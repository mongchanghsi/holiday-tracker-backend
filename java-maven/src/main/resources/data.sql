DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL
);

INSERT INTO user (name, email, password) VALUES
    ('John Doe', 'johndoe@example.com', '12345'),
    ('John Dough', 'johndough@example.com', '12345'),
    ('John Doh', 'johndoh@example.com', '12345');

DROP TABLE IF EXISTS holiday;

CREATE TABLE holiday (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    date VARCHAR(250) NOT NULL
);

INSERT INTO holiday (name, date) VALUES
    ('New Year', '2020-01-01'),
    ('Chinese New Year', '2020-02-02'),
    ('Christmas', '2020-12-25');