INSERT INTO tb_user (is_Active, created_At, email, first_Name, last_Name, password, phone, role, username) VALUES (1, '2023-07-12 10:30:00', 'nicholasboari@gmail.com', 'Nicholas', 'Boari', '$2a$12$t1GAX5n7hnBAhMQFSBi9x.oS.ih2ew5Vib.saMa.R8ZuMwiX28qVq', '992220086', 'USER', 'nicholasboari');

INSERT INTO tb_category (name) VALUES ('Category A');
INSERT INTO tb_category (name) VALUES ('Category B');
INSERT INTO tb_category (name) VALUES ('Category C');

INSERT INTO tb_brand (name) VALUES ('Brand A');
INSERT INTO tb_brand (name) VALUES ('Brand B');
INSERT INTO tb_brand (name) VALUES ('Brand C');

--INSERT INTO tb_product (id, name, price, brand_Id, category_Id)
--SELECT 1, 'Product A', 10, 1, 1 FROM DUAL
--UNION ALL
--SELECT 2, 'Product B', 20, 1, 1 FROM DUAL;
----
--INSERT INTO tb_order (user_Id) VALUES (1);
--INSERT INTO tb_order (user_Id) VALUES (1);
