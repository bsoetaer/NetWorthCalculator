# NetWorthCalculator

Need to be aware of text display room with large numbers
-- Should have enough space. Provide tooltip on hover if user shrinks screen too much so they at least have some way to view them
Need to be aware of numbers exceeding variable size
-- Restrict entry values to reasonable length
-- If totals exceed possible values. Show error to user and highlight that the totals displayed are inaccurate

Frontend Design:
1 Calculator class
* 1 Net Worth Total
* 1 Currency Picker
* 2 Tables, 1 for assets, 1 for liabilities
* Owns data state
** Data preloaded in memory
** Data not saved to file or database. Data changes do not persist across sessions.

Net Worth Total
* Basic text display
* If negative display with negative symbol

Currency Picker
* 10 option dropdown
* Top 10 currencies based on popularity https://www.ig.com/au/trading-strategies/the-top-ten-most-traded-currencies-in-the-world-180904
* Default to Canadian
* Fire off calculation request if currency changes

Tables
* Display currency symbol plus value. Currency symbol known on JS side. No need to get from backend
** Currency symbol should not be part of the editable field
* Disallow negatives. A negative asset is a liability and a negative liability is an asset
* Only the value is editable
* Fire off calculation request once cell loses focus or the user presses enter
** Prevents un-necessary traffic for every character the customer types in
** May make user experience worse until they figure out to click off or press enter
** Potential improvement: Timer to also send off request if field was edited and not deselected but 5 seconds have passed.
* Do not adjust values if user has not changed the currency type, even if the currency rate has changed.

Backend Design
* Single endpoint named "calculate"
* Takes JSON with 3 elements
** currencyType with subElements oldCurrency and newCurrency. This is optional.
** assets. contains values array. Collection of id, value pairs
** liabilities. contains values array. Collection of id, value pairs
* Cache currency rates hourly. Can get rates from https://exchangeratesapi.io/
** Won't get the most up to the minute rates but will provide a better user experience 

Response
* JSON with 3 elements
** assets with values array and a total element
** liabilities with values array and a total element
** netWorth

Error Response
* JSON with message element
** for unable to get exchange rates. Display to user
** for totals exceeding variable size

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
* Base currency type does not change unless editted in that currency type
* Tables render correctly
* Editing a value calls the backend
* Changing a currency calls the backend
* Values update when receiving data from backend
* Larger total than what inputs can hold is returned
* Negative total