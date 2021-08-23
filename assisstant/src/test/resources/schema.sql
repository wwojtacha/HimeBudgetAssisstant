drop table if exists register_accounts;

create table if not exists register_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(19.2) NOT NULL
) ENGINE = InnoDB;