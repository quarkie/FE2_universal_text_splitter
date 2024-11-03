# FE2 Alarmtext splitter

This repository includes a custom parser for the FE2 system.
It is used by the FW ROS Fire Department that receives alarm notifications via fax,Email,POCSAG , Tetra SDS.
Evaluating this information can also be done using the built-in features of FE2 (text parsing, global replacements, alarm processing).
 However, extracting operational resources from the text, such as alerted vehicles, is challenging with these built-in tools.

Functionality
The code is designed to follow the regular FE2 process:

Global replacement using regex (corrects typical text recognition errors).
Text parsing (extracts the main blocks from the alarm fax).
Address recognition (extracts parameters like street, house, city, etc.).
Vehicle recognition: Since this feature is not part of FE2's standard alarm processing, a parameter called vehicles is set here.
This variable must contain the exact vehicle names or codes on a separate line,
which is difficult to extract from the fax without additional logic.
Additionally, a variable is created that includes all operational resources, including other alerted fire departments, formatted for direct use in alarm processing.
Another variable outputs this information in HTML format for easier email distribution.
For keyword recognition and address extraction from coordinates, the existing FE2 functionality is utilized.
In case of an exception, it is not passed to FE2 (the consequences of that are unclear).
Instead, the exception is recorded in a parameter. A functioning alarm is not possible in this case, but it allows for easier identification of the cause.
Ensures that compilation occurs with the correct Java version.
Includes anonymized alarm fax/mail as a unit test.

Usage
Download the latest FE2_Custom_Parser.jar.
Stop FE2.
Copy the file to ...\FE2\Config\data\extern.
Start FE2.
In FE2 Administration, the parser can be selected under Settings > Parser.
Check that the version is correct