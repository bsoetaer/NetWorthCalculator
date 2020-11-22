# NetWorthCalculator

A simple net worth calculator that has both assets and liabilities. It does a simple subtraction to calculate your net worth and allows you to convert all values into different currencies based on the current exchange rate. This is a client and server application. Data is preloaded from the server and data only persists for the duration of the browser session. Reloading the page will reset the data. In addition, each new session is prepopulated with a set of values from a default data set. Items cannot be added, removed, or modified besides their final amount.

This is a development project currently and does not have production artifacts generated.
* To run the frontend: Run "npm start" in the frontend directory.
* To run the backend: Run it from intellij editor or another editor.

Current currencies picked are some of the most traded currencies as of November 2020 and it defaults to being in Canadian currency. Exchange rates are fetched daily from https://exchangeratesapi.io/.
