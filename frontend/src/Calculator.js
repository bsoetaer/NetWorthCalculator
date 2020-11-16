import AccountingTable from './AccountingTable';
import CurrencyPicker from './CurrencyPicker';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
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
        let newState = Object.assign({}, this.state);
        newState.currency.name = currency.name;
        newState.currency.symbol = currency.symbol;
        this.setState(newState);
        this.convert(currency.name, currency.symbol);
    }

    handleValueChanged(event, value, itemType, itemId, valueName) {
        //TODO Maybe change this to object spread?
        let newState = Object.assign({}, this.state);
        let itemToUpdate = newState[itemType.toLowerCase()].find(item => item.id === itemId).values.find(x => x.name === valueName);
        itemToUpdate.value = value;
        this.setState(newState);
        this.updateValue(itemType, itemId, valueName, value);
    }

    componentDidMount()
    {
        this.createSession();
    }

    updateValue(itemType, itemId, valueName, value)
    {
        let update = {
            "currency": this.state.currency.name,
            "name": valueName,
            "value": value,
        }

        let url = new URL("http://localhost:8080/value");
        let params = {userId: this.state.userId, id: itemId};
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

        console.log(url.toString());

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
            .then(() => this.getCategories())
            .catch(err => { alert(err) });
    }

    getCategories()
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
            .then(() => this.convert(this.state.currency.name, this.state.currency.symbol))
            .catch(err => { alert(err) });
    }

    convert(currencyName, currencySymbol)
    {
        let url = new URL("http://localhost:8080/convert");
        let params = {userId: this.state.userId, currency: currencyName};
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

        fetch(url)
            .then(response => this.convertResponse(response))
            .then((data) => {
                let newState = Object.assign({}, this.state);
                newState.currency.name = currencyName;
                newState.currency.symbol = currencySymbol;
                newState.totals = data.totals;
                newState.assets = data.assets;
                newState.liabilities = data.liabilities;
                this.setState(newState);
            })
            .catch(err => { alert(err) });
    }

    calculateTotals()
    {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(this.state)
        };
        fetch('http://localhost:8080/calculate', requestOptions)
            .then(response => response.json())
            .then((data) => this.setState(data))
            .catch(err => { alert(err) });
    }

    getMatchingCategories(items)
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
        const assetCategories = this.getMatchingCategories(this.state.assets);
        const liabilityCategories = this.getMatchingCategories(this.state.liabilities);
        const assetsColumns = [
            {
                Header: 'Name',
                accessor: 'name'
            },
            {
                Header: 'Amount',
                accessor: 'value'
            }
        ];
        const liabilitiesColumns = [
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
                        Net Worth:
                    </TotalGrid>
                    <TotalGrid item xs={6} align="right" >
                        <CurrencyTextField 
                            className={classes.nonedit}
                            readOnly={true}
                            currencySymbol={this.state.currency.symbol} 
                            decimalCharacter="."
                            digitGroupSeparator=","
                            outputFormat="string"
                            value={this.state.totals.netWorth}/>
                    </TotalGrid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            categories={assetCategories}
                            columns={assetsColumns} 
                            data={this.state.assets}
                            total={this.state.totals.assets}
                            name='Assets' 
                            currency={this.state.currency.symbol} 
                            onChange={(e,v,itemId,valueName) => this.handleValueChanged(e, v, 'assets', itemId, valueName)}/>
                    </Grid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            columns={liabilitiesColumns} 
                            data={this.state.liabilities}
                            categories={liabilityCategories}
                            total={this.state.totals.liabilities}
                            name='Liabilities' 
                            currency={this.state.currency.symbol} 
                            onChange={(e,v,itemId,valueName) => this.handleValueChanged(e, v, 'liabilities', itemId, valueName)}/>
                    </Grid>
                </Grid>
            </div>
        )
    }
}

export default withStyles(useStyles)(Calculator);