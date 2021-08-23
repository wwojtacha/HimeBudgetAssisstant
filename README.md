# HomeBudgetAssistant

The purpose of the application is to help manage home budget.
Presently, it is possible to make money transfers between two different register accounts,
recharge an existing register account and list available accounts.

Before starting the application, mysql database named 'home_budget' is required running on port 3306.
Additionally, mysql user named 'assistant' identified by password: 'password' is also required.

In a nutshell, after logging to mysql client:

    create database home_budget;
    create user 'assistant'@'localhost' identified by 'password';
    grant all privileges on home_budget.* to 'assistant'@'localhost';
    flush privileges;

To run the application, go to directory with pom.xml file and type:

    'mvn spring-boot:run'

The application runs on port: 8200. To make requests, using for instance Postman, use url:

    http://localhost:8200

In order to build the application with tests triggered, go to directory with pom.xml file and type:

    'mvn clean install'

#### Bear in mind that tests require a separate database named 'home_budget_test', and the same user with the same privileges as in case of production database.

#### REMARKS ###
=> It would be good to link a register account with a user,

=> I am not sure how to understand the following requirement:

    3. Data persistence: the application should persist balance of each register
    each time it is updated and even in case the whole system is turned off,
    all registers should have their balances set to previous values upon its restart

Does it mean that during each restart 'register_account' table should be recreated with initial values ?
That is how it is done in case of database used for tests.

#### Current understanding/implementation:
If, for instance during money transfer, there is a power shutdown, the source and destination balances will be reset to their values from before the transfer.
However, with each restart 'register_account' table is not recreated and repopulated with initial values.


