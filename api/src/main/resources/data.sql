INSERT INTO tb_address (cep, country, state, street, neighborhood) VALUES ('12060000', 'Brazil', 'SP', 'rua do joaozinho', 'foo');

INSERT INTO tb_user (is_Active, created_At, email, first_Name, last_Name, password, phone, role, username) VALUES (1, '2023-07-12 10:30:00', 'nicholasboari@gmail.com', 'Nicholas', 'Boari', '$2a$12$t1GAX5n7hnBAhMQFSBi9x.oS.ih2ew5Vib.saMa.R8ZuMwiX28qVq', '992220086', 'ADMIN', 'nicholasboari');
INSERT INTO tb_user (address_Id, is_Active, created_At, email, first_Name, last_Name, password, phone, role, username) VALUES (1,1, '2023-07-12 10:30:00', 'joaozinho@gmail.com', 'Joao', 'Silva', '$2a$12$t1GAX5n7hnBAhMQFSBi9x.oS.ih2ew5Vib.saMa.R8ZuMwiX28qVq', '1234455324', 'USER', 'joaozinho');

INSERT INTO tb_category (name) VALUES ('Category A');
INSERT INTO tb_category (name) VALUES ('Category B');
INSERT INTO tb_category (name) VALUES ('Category C');

INSERT INTO tb_brand (name) VALUES ('Brand A');
INSERT INTO tb_brand (name) VALUES ('Brand B');
INSERT INTO tb_brand (name) VALUES ('Brand C');

INSERT INTO tb_product (name, price, image_Url, brand_Id, category_Id) VALUES ('Iphone X', '2000', 'image1.png', 1, 1)
