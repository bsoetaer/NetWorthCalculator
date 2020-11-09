import AccountingTable from './AccountingTable';
import CurrencyPicker from './CurrencyPicker';
const { Component } = require("react");

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
                                baseCurrency: "CAD",
                                name: 'Chequing',
                                value: 2000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Savings for Taxes',
                                value: 4000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Rainy Day Fund',
                                value: 506.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Savings for Fun',
                                value: 5000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Savings for Travel',
                                value: 400.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Savings for Personal Development',
                                value: 200.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Investment 1',
                                value: 5000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Investment 2',
                                value: 60000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Investment 3',
                                value: 2000.00
                            }
                        ]
                    },
                    {
                        category: 'Long Term Assets',
                        values: [
                            {
                                baseCurrency: "CAD",
                                name: 'Primary Home',
                                value: 455000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Second Home',
                                value: 1564321.00
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
                                baseCurrency: "CAD",
                                name: 'Credit Card 1',
                                payment: 200.00,
                                value: 4342.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Credit Card 2',
                                payment: 150.00,
                                value: 322.00
                            }
                        ]
                    },
                    {
                        category: 'Long Term Debt',
                        values: [
                            {
                                baseCurrency: "CAD",
                                name: 'Mortgage 1',
                                payment: 2000.00,
                                value: 250999.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Mortgage 2',
                                payment: 3500.00,
                                value: 632634.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Line of Credit',
                                payment: 500.00,
                                value: 10000.00
                            },
                            {
                                baseCurrency: "CAD",
                                name: 'Investment Loan',
                                payment: 700.00,
                                value: 10000.00
                            }
                        ]
                    }
                ]
            },
            currency: {
                name: "CAD",
                symbol: "$"
            },
            total: 15
        }
    }

    handleCurrencyChanged(currency)
    {
        let newState = Object.assign({}, this.state);
        newState.currency = currency;
        this.setState(newState);
        //TODO Call backend and update everything
    }

    handleValueChanged(event, value, itemType, category, name, element) {
        //TODO Maybe change this to object spread?
        let newState = Object.assign({}, this.state);
        let itemToUpdate = newState[itemType.toLowerCase()].items.find(cat => cat.category === category).values.find(v => v.name === name);
        itemToUpdate[element] = value;
        itemToUpdate.baseCurrency = newState.currency.name;
        this.setState(newState);
        //TODO Call backend and update totals
        //TODO Make sure totals are up to date on first load
    }

    render() {
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
                Header: '',
                accessor: 'value'
            }
        ]
        
        return (
            <div>
                <div>Current Net Worth: {this.state.total} </div>
                <CurrencyPicker
                    currency={this.state.currency.name}
                    onChange={(currency) => this.handleCurrencyChanged(currency)}
                ></CurrencyPicker>
                <div>
                    <AccountingTable 
                        columns={assetsColumns} 
                        data={assetsData} 
                        name='Assets' 
                        currency={this.state.currency.symbol} 
                        onChange={(e,v,c,n,el) => this.handleValueChanged(e, v, 'assets', c, n, el)}/>
                    <AccountingTable 
                        columns={liabilitiesColumns} 
                        data={liabilitiesData} 
                        name='Liabilities' 
                        currency={this.state.currency.symbol} 
                        onChange={(e,v,c,n,el) => this.handleValueChanged(e, v, 'liabilities', c, n, el)}/>
                </div>  
            </div>
        )
    }
}

export default Calculator;