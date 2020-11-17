import React from 'react';
import { makeStyles, withStyles } from '@material-ui/core/styles';
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
    total: {
        '& .MuiInput-underline:before': {
            "border": 0,
            "content": "none"
        },
        '& .MuiInput-underline:after': {
            "border": 0,
            "content": "none"  
        },
        '& .MuiInputAdornment-root:': {
            "font-size": 24
        },
        '.MuiInputAdornment-root:': {
            "font-size": 24
        },
        'MuiInputAdornment-root:': {
            "font-size": 24
        },
        '.MuiInputAdornment-root.element:': {
            "font-size": 24
        },
        'MuiInputAdornment-rootelement:': {
            "font-size": 24
        },
    },
    resize: {
        padding: '4px 16px 4px 16px',
        fontSize: 24
    }
});

const ItemTableCell = withStyles((theme) => ({
    root: {
        padding: '4px 16px 4px 16px'
    }
  }))(TableCell);

// TODO Make fontSize apply to the currency symbol
const TotalTableCell = withStyles((theme) => ({
    root: {
        padding: '12px 16px 12px 16px',
        fontSize: '1.2rem',
        background: '#AED6F1'
    }
  }))(TableCell);

const HeaderTableCell = withStyles((theme) => ({
    root: {
        padding: '8px 16px 8px 16px',
        fontSize: '1.1rem',
        background: '#D0ECE7'
    }
}))(TableCell);

function onKeyPress(event) {
    if(event.key === "Enter") {
        event.target.blur();
    }
}

function AccountingTable(props) {
    const classes = useStyles();

    return (
        <Paper className={classes.root}>
            <Table className={classes.table} >
                <TableHead key = "TableType"  >
                    <TableRow padding="4px">
                        <TotalTableCell key="Name" align='left'>
                            {props.name}
                        </TotalTableCell>
                        <TotalTableCell key="Value" align='right' colSpan={props.columns.length - 1}>
                            <CurrencyTextField 
                                className={classes.total}
                                currencySymbol={props.currency}
                                decimalCharacter="."
                                digitGroupSeparator=","
                                outputFormat="string"
                                readOnly={true}
                                value={props.total}
                            />
                        </TotalTableCell>
                    </TableRow>
                </TableHead>
                {props.categories.map(category => ([
                    <TableHead key ={category.id + "Header"}>
                        <TableRow key = {category.id}>
                            <HeaderTableCell key = "CategoryName">{category.name}</HeaderTableCell>
                            {props.columns.map(col => {
                                if(col.Header === 'Name')
                                    return null
                                return <HeaderTableCell align={col.accessor === 'value' ? "right" : "center"} key={col.accessor}>{col.Header}</HeaderTableCell>
                            })}
                        </TableRow>
                    </TableHead>,
                    <TableBody key = {category.id + "Body"}>
                        {props.data.map(row => (
                            (row.category !== category.id) ? null :
                            <TableRow key = {row.id}>
                                <ItemTableCell align="left" key="name" height={3}>
                                    {row.name}
                                </ItemTableCell>
                                {props.columns.map(col => (
                                    (col.Header === 'Name') ? null :
                                    <ItemTableCell align={col.accessor === 'value' ? "right" : "center"} key = {col.accessor}>
                                        <CurrencyTextField 
                                            className={(col.accessor === 'value' ? classes.edit : classes.nonedit)}
                                            currencySymbol={props.currency}
                                            decimalCharacter="."
                                            digitGroupSeparator=","
                                            minimumValue="0"
                                            onBlur={(e, v) => props.onChange(e, v, row.id, col.accessor)}
                                            onKeyPress={(e) => onKeyPress(e)}
                                            outputFormat="string"
                                            readOnly={(col.accessor !== 'value')}
                                            value={row.values.find(e => e.name === col.accessor).value}
                                        />
                                    </ItemTableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                ]))}
            </Table>
        </Paper>
    );
}

export default AccountingTable;