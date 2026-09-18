Unique QA Test Cases – Java Tax Calculator

Overview

This README contains unique validation, boundary, and negative test cases for the Java Tax Calculator. The focus is on testing different user flows and avoiding repeated happy-flow test cases.

TC01---------

Test Case: Valid item entry

Input:

Item name = rice

Item price = 100

Quantity = 1

Item type = Raw

Expected Output: Item is accepted successfully, and tax and final price are calculated correctly.

TC02---------

Test Case: Empty item name

Input: name = ""

Expected Output: Item name cannot be empty.

TC03---------

Test Case: Blank-space item name

Input: name = "   "

Expected Output: Item name cannot be empty.

TC04---------

Test Case: Negative item price

Input: price = -100

Expected Output: Price must be greater than 0.

TC05---------

Test Case: Zero item price

Input: price = 0

Expected Output: Price must be greater than 0.

TC06---------

Test Case: Negative quantity

Input: quantity = -2

Expected Output: Quantity must be greater than 0.

TC07---------

Test Case: Zero quantity

Input: quantity = 0

Expected Output: Quantity must be greater than 0.

TC08---------

Test Case: Invalid item type

Input: type = electronic

Expected Output: Invalid item type.

TC09---------

Test Case: Duplicate item with the same type

Input:

First item: name = rice, type = Raw

Second item: name = rice, type = Raw

Expected Output: Same item already exists in DB.

TC10---------

Test Case: Same item name with a different type

Input:

First item: name = rice, type = Raw

Second item: name = rice, type = Imported

Expected Output: Both items are accepted successfully.

TC11---------

Test Case: Invalid yes/no response

Input: response = maybe

Expected Output: Invalid response. Please enter y or n.

TC12---------

Test Case: Exit without entering any item

Input: response = n

Expected Output: Program exits without displaying item details.

TC13---------

Test Case: Imported item with final cost less than or equal to 100

Input:

Item price = 90

Quantity = 1

Item type = Imported

Expected Output: Import duty is calculated, and surcharge is 5 when the final cost is less than or equal to 100.

TC14---------

Test Case: Imported item with final cost between 100 and 200

Input:

Item price = 100

Quantity = 1

Item type = Imported

Expected Output: Import duty is calculated, and surcharge is 10 when the final cost is greater than 100 and less than or equal to 200.

TC15---------

Test Case: Imported item with final cost greater than 200

Input:

Item price = 300

Quantity = 1

Item type = Imported

Expected Output: Import duty is calculated, and surcharge is 5% of the final cost.

TC16---------

Test Case: Invalid item followed by a valid item

Input:

First item: price = -100

Second item: valid item details

Expected Output: The error message is displayed for the invalid item, and the valid item can be entered afterward.

TC17---------

Test Case: Failed item entry is not stored

Input: Enter an invalid or duplicate item.

Expected Output: The invalid item is not added to the item list or duplicate map.

TC18---------

Test Case: Non-numeric price

Input: price = abc

Expected Output: The program should display an input error instead of terminating.

Actual Result: The current code throws InputMismatchException because it does not handle non-numeric price input.
