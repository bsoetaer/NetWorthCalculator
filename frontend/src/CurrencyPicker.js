import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const currencies = [
    {
        name: 'AUD',
        symbol: '$'
    },
    {
        name: 'CAD',
        symbol: ''
    },
    {
        name: 'CHF',
        symbol: 'Fr.'
    },
    {
        name: 'CNH',
        symbol: '¥'
    },
    {
        name: 'EUR',
        symbol: '€'
    },
    {
        name: 'GBP',
        symbol: '£'
    },
    {
        name: 'JPY',
        symbol: '¥'
    },
    {
        name: 'NZD',
        symbol: '$'
    },
    {
        name: 'SEK',
        symbol: 'kr'
    },
    {
        name: 'USD',
        symbol: '$'
    },
]

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
}));

export default function CurrencyPicker(props) {
  const classes = useStyles();

  const handleChange = (event, onChange) => {
      let currency = currencies.find(cur => cur.name === event.target.value);
      onChange(currency);
  };

  return (
    <FormControl className={classes.formControl}>
        <InputLabel id="demo-simple-select-label">Select Currency</InputLabel>
        <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={props.currency}
            onChange={(e) => handleChange(e, props.onChange)}
        >
            {currencies.map(cur => (
                <MenuItem key={cur.name} value={cur.name}>{cur.name}</MenuItem>
            ))}
        </Select>
    </FormControl>
  );
}
