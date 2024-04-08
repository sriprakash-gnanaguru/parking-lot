CREATE TABLE street (street_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       street_name VARCHAR(50) NOT NULL,
                       price_per_min INT NOT NULL);

INSERT INTO street (street_name, price_per_min)
VALUES
    ('Java', 15),
    ('Jakarta', 13),
    ('Spring', 10),
    ('Azure', 10);
