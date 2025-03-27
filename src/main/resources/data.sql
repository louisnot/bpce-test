CREATE TABLE Room (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      capacity INT NOT NULL,
                      ecran BOOLEAN NOT NULL,
                      pieuvre BOOLEAN NOT NULL,
                      webcam BOOLEAN NOT NULL,
                      tableau BOOLEAN NOT NULL
);

INSERT INTO Room (name, capacity, ecran, pieuvre, webcam, tableau) VALUES
('E1001', 23, false, false, false, false),
('E1002', 10, true, false, false, false),
('E1003', 8, false, true, false, false),
('E1004', 4, false, false, false, true),
('E2001', 4, false, false, false, false),
('E2002', 15, true, false, true, false),
('E2003', 7, false, false, false, false),
('E2004', 9, false, false, false, true),
('E3001', 13, true, true, true, false),
('E3002', 8, false, false, false, false),
('E3003', 9, true, true, false, false),
('E3004', 4, false, false, false, false);




