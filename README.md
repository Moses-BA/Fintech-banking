# Fintech-banking
Purpose:

This code loads accounts and applies transactions from two text files. The first file contains account information, including the account type, account number, and initial balance. The second file contains transaction information, including the account number, transaction type, and amount.

Usage:

To run the code, simply compile and execute the Main class. The code will load the accounts from the data/accounts.txt file and apply the transactions from the data/transactions.txt file.

Example:

$ javac Main.java
$ java Main

Account A1234B Balance: 10000.00
Account E3456F Balance: 5000.00
Account I5678J Balance: 2500.00
Account C2345D Credit: 20000.00
Account G4567H Credit: 15000.00
Notes:

The code assumes that the text files are formatted correctly. The accounts file should have one line per account, with the account type, account number, and initial balance separated by spaces. The transactions file should also have one line per transaction, with the account number, transaction type, and amount separated by spaces.
The code does not validate the input data. It is important to make sure that the data is correct before running the code.
The code is only a simple example. It can be modified to meet your specific needs. For example, you could add additional validation checks or add support for additional account types and transaction types.
