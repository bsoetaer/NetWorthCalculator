# NetWorthCalculator

A simple net worth calculator that has both assets and liabilities. Data is preloaded from the server and data only persists for the duration of the browser session. Reloading the page will reset the data.

To run the frontend: Run "npm start" in the frontend directory.
To run the backend: Run it from intellij editor or another editor.

Frontend design
* Similar layout but how it gets the data is different
* Rather than monolithic state, its broken down into assets, categories, liabilities, currency, and totals.
* On load call backend to get values. 2 calls, 1 for convert to get all values and 1 for categories
* On update, only update relevant parts of state.
* Check responseId to make sure we do not update with old values. Newest response id wins

Backend design
* Owns data. In static class for now. Change to database/file could be a future improvement.
* static file generates default data and stores it.
* each endpoint includes an incrementing responseId in the response. This is in case multiple requests are received at once.
* 4 endpoints
* user - generates a user session and returns a user id. This user id must be passed to all other endpoints to get that user's specific data.
* value - Update only. Takes an item id, and a bool of whether this is an asset or a liability. Updates value in dataset and returns all 3 totals
* convert - GET only. Takes a currency as a string. Returns list of totals, assets, and liabilities. Each value (asset/liability) has an id, and a category id.
* category - GET only. Takes a bool of whether to get asset or liability categories. Returns list of category names/ids for the given type.

Currency Picker
* 10 option dropdown
* Top 10 currencies based on popularity https://www.ig.com/au/trading-strategies/the-top-ten-most-traded-currencies-in-the-world-180904
* Default to Canadian
* Fire off calculation request if currency changes

Discussion points
* What assumptions did you make when implementing your solution and what impact did they have on the design?
Currency symbols will not change so they can just be hardcoded.
Values bigger than what a max double can hold do not need to be supported. This simplifies the arithmetic.

* How would you improve the performance of your application?
Remove the backend altogether and go directly to the exchange rate service. The backend is un-necessary in this situation and if I was starting from scratch I would not have a backend
and only add a backend in later if new requirements arise that require it.
Cache the currency rates to avoid repeatedly getting the currency.
If the backend was forced to stay there. One way to increase performance would be to implement a "Calculate" button. Rather than updating on each cell edit, only update when the button is pressed.
* How would you go about testing your application?
* How would you make your service secure?
Currently the service is using basic HTTP to communicate between the front and backend. If the backend still exists, I would start by changing this to HTTPS. The backend is just a glorified calculator
with very limited functionality so I would not implement authorization on it currently. If the backend was storing the information about the user's net worth, then authorization should be put in place
to prevent other people accessing their information. 
* How would you globalize your application?

* How would you make your application more resilient to errors? (network, upstream services, etc)
* How would you take advantage of HTTP caching?
Could use HTTP caching for the currency rate conversions. If a user is just jumping between different currencies without editing the field values, cache the results instead of re-calculating each time.
* How would you support multiple users editing concurrently?
Current implementation is stateless and the data is only unique to a single session. Each user to access the webpage will get a separate instance of the javascript so this is not a concern. Is this true?
If the application had state and we persisted data across sessions, then could do a couple things depending on the use case. 

Frontend What to test:
* All currency symbols update when currency picker changes
* Entered value is always exact when currency is the same as when it was entered. No drifting currency caused by too many conversions.
* Tables render correctly
* Editing a value calls the backend and updates the value/totals
* Changing a currency calls the backend and updates all values/totals
* Larger total than what inputs can hold is returned
* Negative total