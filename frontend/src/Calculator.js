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
            assets: {
                total: 7,
                items: [
                    {
                        category: 'Cash and Investments',
                        values: [
                            {
                                name: 'Chequing',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 2000.00,
                                        name: 'value',
                                        value: 2000.00
                                    }
                                ]
                            },
                            {
                                name: 'Savings for Taxes',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 4000.00,
                                        name: 'value',
                                        value: 4000.00
                                    }
                                ]
                            },
                            {
                                name: 'Rainy Day Fund',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 506.00,
                                        name: 'value',
                                        value: 506.00
                                    }
                                ]
                            },
                            {
                                name: 'Savings for Fun',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 5000.00,
                                        name: 'value',
                                        value: 5000.00
                                    }
                                ]
                            },
                            {
                                name: 'Savings for Travel',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 400.00,
                                        name: 'value',
                                        value: 400.00
                                    }
                                ]
                            },
                            {
                                name: 'Savings for Personal Development',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 200.00,
                                        name: 'value',
                                        value: 200.00
                                    }
                                ]
                            },
                            {
                                name: 'Investment 1',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 5000.00,
                                        name: 'value',
                                        value: 5000.00
                                    }
                                ]
                            },
                            {
                                name: 'Investment 2',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 60000.00,
                                        name: 'value',
                                        value: 60000.00
                                    }
                                ]
                            },
                            {
                                name: 'Investment 3',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 2000.00,
                                        name: 'value',
                                        value: 2000.00
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        category: 'Long Term Assets',
                        values: [
                            {
                                name: 'Primary Home',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 455000.00,
                                        name: 'value',
                                        value: 455000.00
                                    }
                                ]
                            },
                            {
                                name: 'Second Home',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 1564321.00,
                                        name: 'value',
                                        value: 1564321.00
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            liabilities: {
                total: 5,
                items: [
                    {
                        category: 'Short Term Liabilities',
                        values: [
                            {
                                name: 'Credit Card 1',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 4342.00,
                                        name: 'value',
                                        value: 4342.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 200.00,
                                        name: 'payment',
                                        value: 200.00
                                    },
                                ]
                            },
                            {
                                name: 'Credit Card 2',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 322.00,
                                        name: 'value',
                                        value: 322.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 150.00,
                                        name: 'payment',
                                        value: 150.00
                                    },
                                ]
                            }
                        ]
                    },
                    {
                        category: 'Long Term Debt',
                        values: [
                            {
                                name: 'Mortgage 1',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 250999.00,
                                        name: 'value',
                                        value: 250999.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 2000.00,
                                        name: 'payment',
                                        value: 2000.00
                                    },
                                ]
                            },
                            {
                                name: 'Mortgage 2',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 632634.00,
                                        name: 'value',
                                        value: 632634.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 3500.00,
                                        name: 'payment',
                                        value: 3500.00
                                    },
                                ]
                            },
                            {
                                name: 'Line of Credit',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 10000.00,
                                        name: 'value',
                                        value: 10000.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 500.00,
                                        name: 'payment',
                                        value: 500.00
                                    },
                                ]
                            },
                            {
                                name: 'Investment Loan',
                                values: [
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 10000.00,
                                        name: 'value',
                                        value: 10000.00
                                    },
                                    {
                                        baseCurrency: 'CAD',
                                        baseValue: 700.00,
                                        name: 'payment',
                                        value: 700.00
                                    },
                                ]
                            }
                        ]
                    }
                ]
            },
            currency: {
                name: 'CAD',
                symbol: '$'
            },
            total: 15
        }
    }

    handleCurrencyChanged(currency)
    {
        let newState = Object.assign({}, this.state);
        newState.currency.name = currency.name;
        newState.currency.symbol = currency.symbol;
        this.setState(newState);
        this.calculateTotals();
        //TODO Call backend and update everything
    }

    handleValueChanged(event, value, itemType, category, name, element) {
        //TODO Maybe change this to object spread?
        let newState = Object.assign({}, this.state);
        let itemToUpdate = newState[itemType.toLowerCase()].items.find(cat => cat.category === category).values.find(v => v.name === name).values.find(x => x.name === element);
        itemToUpdate.baseValue = value;
        itemToUpdate.baseCurrency = newState.currency.name;
        itemToUpdate.value = value;
        this.setState(newState);
        this.calculateTotals();
        //TODO Call backend and update totals
        //TODO Make sure totals are up to date on first load
    }

    componentDidMount()
    {
        this.calculateTotals();
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

    render() {
        const { classes } = this.props;
        const assetsData = this.state.assets;
        const liabilitiesData = this.state.liabilities;
        const assetsColumns = [
            {
                Header: 'Name',
                accessor: 'name'
            },
            {
                Header: '',
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
                            value={this.state.total}/>
                    </TotalGrid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            columns={assetsColumns} 
                            data={assetsData} 
                            name='Assets' 
                            currency={this.state.currency.symbol} 
                            onChange={(e,v,c,n,el) => this.handleValueChanged(e, v, 'assets', c, n, el)}/>
                    </Grid>
                    <Grid item xs={6}>
                        <AccountingTable 
                            columns={liabilitiesColumns} 
                            data={liabilitiesData} 
                            name='Liabilities' 
                            currency={this.state.currency.symbol} 
                            onChange={(e,v,c,n,el) => this.handleValueChanged(e, v, 'liabilities', c, n, el)}/>
                    </Grid>
                </Grid>
            </div>
        )
    }
}

export default withStyles(useStyles)(Calculator);