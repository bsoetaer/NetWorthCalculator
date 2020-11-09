import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import CurrencyTextField from '@unicef/material-ui-currency-textfield'

const useStyles = makeStyles({
    root: {
        width: '100%',
        marginTop: 3,
        overflowX: 'auto',
    },
    table: {
        minWidth: 700,
    },
    nonedit: {
        '& .MuiInput-underline:before': {
            "border": 0,
            "content": "none"
        },
        '& .MuiInput-underline:after': {
            "border": 0,
            "content": "none"  
        },
    },
    edit: {}
});

function onKeyPress(event) {
    if(event.key === "Enter") {
        event.target.blur();
    }
}

function AccountingTable(props) {
    const classes = useStyles();

    return (
        <Paper className={classes.root}>
            <Table className={classes.table}>
                <TableHead key = "TableType">
                    <TableRow>
                        <TableCell>{props.name}</TableCell>
                    </TableRow>
                </TableHead>
                {props.data.items.map(category => ([
                    <TableHead key ={category.category + "Header"}>
                        <TableRow key = {category.category}>
                            <TableCell key = "CategoryName">{category.category}</TableCell>
                            {props.columns.map(col => {
                                if(col.Header === 'Name')
                                    return null
                                return <TableCell key = {col.accessor}>{col.Header}</TableCell>
                            })}
                        </TableRow>
                    </TableHead>,
                    <TableBody key = {category.category + "Body"}>
                        {category.values.map(row => (
                            <TableRow key = {row.name}>
                                {props.columns.map(col => (
                                    <TableCell align="left" key = {col.accessor}>
                                        {(col.Header === 'Name') ?
                                            row[col.accessor] :
                                            <CurrencyTextField 
                                                className={(col.accessor === 'value' ? classes.edit : classes.nonedit)}
                                                readOnly={(col.accessor !== 'value')}
                                                currencySymbol={props.currency}
                                                decimalCharacter="."
                                                digitGroupSeparator=","
                                                minimumValue="0"
                                                onBlur={(e, v) => props.onChange(e, v, category.category, row['name'], col.accessor)}
                                                onKeyPress={(e) => onKeyPress(e)}
                                                outputFormat="string"
                                                value={row[col.accessor]}>
                                            </CurrencyTextField>
                                        }
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                ]))}
                <TableBody key = "Total">
                    <TableRow key = {'Total ' + props.name}>
                        <TableCell key = "Name">{'Total ' + props.name}</TableCell>
                        {props.columns.map(col => (
                            (col.accessor !== 'name' && col.accessor !== 'value') ?
                            <TableCell key = {col.accessor + "Spacer"}></TableCell> :
                            null
                        ))}
                        <TableCell key = "Value">
                            <CurrencyTextField 
                                className={classes.nonedit}
                                readOnly={true}
                                currencySymbol={props.currency}
                                decimalCharacter="."
                                digitGroupSeparator=","
                                outputFormat="string"
                                value={props.data.total}>
                            </CurrencyTextField>
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </Paper>
    );
}

export default AccountingTable;