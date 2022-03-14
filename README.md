# SDE_Code_Exercise

It is a project out of the box, the only library that was added was Gson

How to build:
  -Download all gradle dependencies
  -To test it, it is recommended to put a .json file in the 'Download' folder but it can be in any folder that the file chooser has access to
  -Run 'app'
 
How does it work:

  -The application looks for a .json file using a file chooser in case it fails, it loads a file from the 'assets' folder. 
   The UI only has 2 components, a button to reset the application and a recyclerview to display the drivers, 
   when a driver is selected from the list a dialog is shown with the shipment proposal, if he accepts it, the shipment is assigned and 
   an icon appears to the right of the name of the driver, if he rejects it, the dialog just goes away.
   

I assumed that to test it it was going to be tested with another file, 
so I didn't want to tie it to a specific path or name, so I chose a file chooser through intents.
I used Gson for convenience and to validate with the error if the structure was not as expected.
For business rules, they are strings, I ussualy work with regex to deal with strings problems.
I had doubts if the assignment was automatic or on request, the word 'offer' made me think that the assignment was on request
and the driver have the option the take or reject the shipment.  
