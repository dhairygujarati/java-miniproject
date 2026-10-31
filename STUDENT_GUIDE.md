# MiniBank project guide

## What makes this version useful
Alongside the banking functions, this version includes a **Savings Goal Tracker**. A user can attach a target amount to an account, check progress as the balance changes, and see how much remains. The goal is stored in `data/savings-goals.properties`.

## Run on Windows
1. Install JDK 17 or newer.
2. Extract the ZIP.
3. Open the extracted `MiniBank-Complete` folder.
4. Click the folder address bar, type `cmd`, and press Enter.
5. Run `build.bat`.
6. Run `java -cp out minibank.MiniBank`.

## Run on macOS/Linux
Open Terminal in the project folder and run:

```sh
chmod +x build.sh
./build.sh
java -cp out minibank.MiniBank
```

## Savings goal demo
1. Open an account (menu option 1) and note the generated account number.
2. Set a target with option 13.
3. Deposit money with option 2.
4. View the updated progress with option 14.

## Viva preparation
Be ready to explain classes and objects, encapsulation, inheritance, polymorphism, interfaces, exception handling, collections, file handling, and how the savings goal is stored in a `Properties` file. This is a classroom project only; do not enter real banking or personal data.
