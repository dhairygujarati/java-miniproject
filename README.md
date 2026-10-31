# MiniBank — Java Console Banking Project

MiniBank is an educational, menu-driven banking application built in Java 17+. It combines the concepts covered by Labs 01–14 into one interactive console program. The application asks the operator to enter customer details, account type, amounts, and menu choices at runtime. It does not import or depend on an external dataset.

## Main application features

- Open Savings, Current, and Fixed Deposit accounts.
- Deposit, withdraw, transfer funds, view all accounts, search by account number, and print account statements.
- Close an account while retaining it for viewing/audit purposes.
- Validate email, Indian mobile number, PAN, and IFSC formats.
- Sort accounts by balance and generate an end-of-day transaction report.
- **Savings Goal Tracker:** set a target for an account, view a progress bar, and see the amount remaining. Goals are saved locally between sessions.
- Save and reload account state from local files.
- Generate customer/account identifiers and maintain an append-only transaction log.
- Encapsulate account balances and enforce account-type withdrawal rules.
- Demonstrate classes, constructors, inheritance, abstract classes, interfaces, records, enums, switch expressions, nested classes, cloning, `toString`, `equals`, `hashCode`, `Comparable`, and `Comparator`.
- Include custom checked exceptions, annotation-based validation with reflection, a generic repository, collections, thread-safe account storage, synchronized balance operations, and a thread-pool helper.
- Include a separate source-code example and `LAB.md` in each `labs/lab-01` through `labs/lab-13` folder.

## Project layout

```text
MiniBank-Complete/
├── src/main/java/minibank/       # Integrated interactive application
│   ├── MiniBank.java
│   ├── model/
│   ├── model/annotation/
│   ├── exception/
│   ├── service/
│   └── util/
├── labs/
│   ├── lab-01/src/main/java/lab01/
│   ├── lab-02/src/main/java/lab02/
│   ├── ...
│   ├── lab-13/src/main/java/lab13/
│   └── lab-14/src/main/java/lab14/
├── .gitignore
├── README.md
├── build.sh
└── build.bat
```

Each lab directory contains its own Java demo source and a `LAB.md` explaining the covered topic and how to run that lab example. Practical 14 also includes a service-layer facade and a JUnit test-plan note. The root `src` directory is the integrated application; the lab demos are learning examples and use separate packages so they can coexist.

## Requirements

- JDK 17 or newer
- Terminal / command prompt
- No external libraries or network connection required to compile or run

## Compile and run the integrated application

macOS/Linux:

```bash
./build.sh
java -cp out minibank.MiniBank
```

Windows Command Prompt:

```bat
build.bat
java -cp out minibank.MiniBank
```

The application displays a menu. Choose **Open account** and enter details when prompted. Use options 13 and 14 to set and check savings goals. Use the generated account number for later deposits, withdrawals, transfers, searches, and statements. Choose **0** to save and exit.

## Runtime files

The application creates these files relative to the current working directory:

- `data/accounts.dat` — serialized account state
- `data/logs/transactions.log` — transaction log
- `data/report.txt` — generated report
- `data/savings-goals.properties` — saved savings targets by account number

These generated files are excluded by `.gitignore`. The application can create them when it runs; no prefilled customer dataset is shipped.

## Lab coverage

- **Lab 01:** Console menu, `BankInfo` record, menu enum, `Scanner`, switch expression, input retry.
- **Lab 02:** `Customer` and `Account`, private fields, generated IDs, constructors, deposits and withdrawals.
- **Lab 03:** Readable object output, equality/hash code, nested `Address`, cloning, `instanceof`.
- **Lab 04:** Regex validation, transaction command record/parser, statement formatting.
- **Lab 05:** Abstract account hierarchy and polymorphic Savings, Current, and Fixed Deposit accounts.
- **Lab 06:** Interfaces, default methods, functional interface/lambda, marker interface and packages.
- **Lab 07:** Runtime annotations and reflection-based validation.
- **Lab 08:** Checked banking exceptions and structured error handling.
- **Lab 09:** Runnable workers, joining threads, synchronized account operations.
- **Lab 10:** Executor service and managed transaction tasks.
- **Lab 11:** Serialization, file-based transaction logs, NIO report generation.
- **Lab 12:** Map-based account storage, transaction history, thread-safe map, sorting.
- **Lab 13:** Generic repository CRUD, `@Id` reflection, varargs and bounded generic method.
- **Lab 14:** Capstone integration, service-layer facade, test plan, persistence and runnable JAR.

## Important limitations

This is a classroom/demo application, not production banking software. It has no authentication, encrypted storage, database, real bank connectivity, payment rails, or regulatory controls. Do not enter real sensitive customer information. Java object serialization is intended only for this application's own local files; never load untrusted serialized files.
