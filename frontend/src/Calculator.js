import AccountingTable from './AccountingTable';
import CurrencyPicker from './CurrencyPicker';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import CurrencyTextField from '@unicef/material-ui-currency-textfield'
const { Component } = require("react");

const useStyles = theme => ({
    nonedit: {
        '& .MuiInput-underline:before': {
            "border": 0,
            "content": "none"
        },
        '& .MuiInput-underline:after': {
            "border": 0,
            "content": "none"  
        },
    }
});

const TotalGrid = withStyles((theme) => ({
    item: {
        padding: '16px 16px 16px 16px',
        fontSize: '1.3rem',
        background: '#F2F4F4'
    }
  }))(Grid);

class Calculator extends Component {

    static assetColumns = [
        {
            Header: 'Name',
            accessor: 'name'
        },
        {
            Header: 'Amount',
            accessor: 'value'
        }
    ];

    static liabilityColumns = [
        {
            Header: 'Name',
            accessor: 'name'
        },
        {
            Header: 'Monthly Payments',
            accessor: 'payment'
        },
        {
            Header: 'Amount',
            accessor: 'value'
        }
    ]

    constructor(props) {
        super(props);
        this.state = {
            totals: {
                "assets": 0.00,
                "liabilities": 0.00,
                "netWorth": 0.00
            },
            assets: [],
            liabilities: [],
            categories: [],
            currency: {
                name: 'CAD',
                symbol: '$'
            },
            userId: -1
        }
    }

    handleCurrencyChanged(currency)
    {
        let url = new URL("http://localhost:8080/convert");
        let params = {userId: this.state.userId, currency: currency.name};
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

        fetch(url)
            .then(response => this.convertResponse(response))
            .then((data) => {
                let newState = Object.assign({}, this.state);
                newState.currency.name = currency.name;
                newState.currency.symbol = currency.symbol;
                newState.totals = data.totals;
                newState.assets = data.assets;
                newState.liabilities = data.liabilities;
                this.setState(newState);
            })
            .catch(err => { alert(err) });
    }

    handleValueChanged(event, value, itemType, itemId, valueName) {
        let update = {
            "currency": this.state.currency.name,
            "name": valueName,
            "value": value,
        }

        let url = new URL("http://localhost:8080/value");
        let params = {userId: this.state.userId, id: itemId};
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

        const requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(update)
        };
        fetch(url, requestOptions)
            .then(response => this.convertResponse(response))
            .then((data) => {
                let newState = Object.assign({}, this.state);
                let itemToUpdate = newState[itemType.toLowerCase()].find(item => item.id === itemId).values.find(x => x.name === valueName);
                itemToUpdate.value = value;
                newState.totals = data;
                this.setState(newState);
            })
            .catch(err => { alert(err) });
    }

    componentDidMount()
    {
        this.createSession();
    }

    convertResponse(response) {
        if(response.ok) {
            return response.json();
        } else {
            return response.json().then(data => {
                throw Error(data.message)});
        }
    }

    createSession()
    {
        const requestOptions = {
            method: 'POST'
        };
        fetch('http://localhost:8080/user', requestOptions)
            .then(response => this.convertResponse(response))
            .then((data) => {
                let newState = Object.assign({}, this.state);
                newState.userId = data.userId;
                this.setState(newState);
            })
            .then(() => this.fetchCategories())
            .catch(err => { alert(err) });
    }

    fetchCategories()
    {
        let url = new URL("http://localhost:8080/categories");
        let params = {userId:this.state.userId};
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

        fetch(url)
            .then(response => this.convertResponse(response))
            .then((data) => {
                let newState = Object.assign({}, this.state);
                newState.categories = data;
                this.setState(newState);
            })
            .then(() => this.handleCurrencyChanged(this.state.currency))
            .catch(err => { alert(err) });
    }

    matchingCategories(items)
    {
        let itemCategoryIds = new Set();
        items.forEach(item => {
            itemCategoryIds.add(item.category)
        });

        let matchingCategories = [];
        this.state.categories.forEach(cat => {
            if(itemCategoryIds.has(cat.id))
            {
                matchingCategories.push(cat);
            }
        });

        return matchingCategories;
    }

    render() {
        const { classes } = this.props;
        const assetCategories = this.matchingCategories(this.state.assets);
        const liabilityCategories = this.matchingCategories(this.state.liabilities);
        
        return (
            <div>
                <Grid container >
                    <Grid item xs={12} align="center">
                        <CurrencyPicker
                            currency={this.state.currency.name}
                            onChange={(currency) => this.handleCurrencyChanged(currency)}
                        />
                    </Grid>
                    <TotalGrid item xs={6} align="left" padding="16px">
                        Net Worth
                    </TotalGrid>
                    <TotalGrid item xs={6} align="right" >
                        <CurrencyTextField 
                            className={classes.nonedit}
                            currencySymbol={this.state.currency.symbol} 
                            decimalCharacter="."
                            digitGroupSeparator=","
                            maximumValue="10000000000000000" // 3 more 0s than individual values support.
                            outputFormat="string"
                            readOnly={true}
                            value={this.state.totals.netWorth}
                        />
                    </TotalGrid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            categories={assetCategories}
                            columns={Calculator.assetColumns} 
                            currency={this.state.currency.symbol} 
                            data={this.state.assets}
                            name='Assets'
                            onChange={(e,v,itemId,valueName) => this.handleValueChanged(e, v, 'assets', itemId, valueName)}
                            total={this.state.totals.assets}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            categories={liabilityCategories}
                            columns={Calculator.liabilityColumns} 
                            currency={this.state.currency.symbol} 
                            data={this.state.liabilities}
                            name='Liabilities' 
                            onChange={(e,v,itemId,valueName) => this.handleValueChanged(e, v, 'liabilities', itemId, valueName)}
                            total={this.state.totals.liabilities}
                        />
                    </Grid>
                </Grid>
            </div>
        )
    }
}

export default withStyles(useStyles)(Calculator);