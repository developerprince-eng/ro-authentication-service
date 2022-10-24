INSERT INTO public.role(
	role_id, description)
	VALUES
	('ADMIN', 'Administrator'),
	('SITE_MANGER','Site Manager'),
    ('SITE_DISPENER','Dispenser for Site'),
    ('SITE_LAB','Lab Prationer for Site'),
    ('SITE_RECEPT','Receptionist for Site'),
    ('GENERAL','General Stuff'),
    ('SYS_ADMIN', 'System Adminstrator')
	;


INSERT INTO public.permission(
	permission_id, description)
	VALUES
    ('ALLPERMS','Administrative Persmission'),
	('VWSITBYO', 'View Site Activity in Bulawayo'),
	('EDSITBYO', 'Edit Site Activity in Bulawayo'),
	('VWSITZVS', 'View Site Activity in Zvishavane'),
	('EDSITZVS', 'Edit Site Activity in Zvishavane'),
	('VWSITGWR', 'View Site Activity in Gweru'),
	('EDSITGWR', 'Edit Site Activity in Gweru'),
	('EDBKBYO', 'View Bookings in Bulawayo'),
	('VWBKBYO', 'Edit Bookings in Bulawayo'),
	('EDBKGWR', 'View Bookings in Gweru'),
	('VWBKGWR', 'Edit Bookings in Gweru'),
	('EDBKZVS', 'View Bookings in Zvishavane'),
	('VWBKZVS', 'Edit Bookings in Zvishavane'),
    ('GENWORK', 'General Work in All sites')
    ;


INSERT INTO public.role_map(
	role_map_id, branch_id, organisational_id, role_id)
	VALUES
	(1, 'RO-001', 1, 'ADMIN'),
	(2, 'RO-002', 1, 'ADMIN'),
	(3, 'RO-003', 1, 'ADMIN'),
    (4, 'RO-001', 1, 'SITE_MANGER'),
    (5, 'RO-002', 1, 'SITE_MANGER'),
    (6, 'RO-003', 1, 'SITE_MANGER'),
    (7, 'RO-001', 1, 'SITE_DISPENER'),
    (8, 'RO-002', 1, 'SITE_DISPENER'),
    (9, 'RO-003', 1, 'SITE_DISPENER'),
    (10, 'RO-001', 1, 'SITE_RECEPT'),
    (11, 'RO-002', 1, 'SITE_RECEPT'),
    (12, 'RO-003', 1, 'SITE_RECEPT'),
    (13, 'RO-001', 1, 'GENERAL'),
    (14, 'RO-002', 1, 'GENERAL'),
    (15, 'RO-003', 1, 'GENERAL'),
    (16, 'RO-001', 1, 'SYS_ADMIN'),
	(17, 'RO-002', 1, 'SYS_ADMIN'),
	(18, 'RO-003', 1, 'SYS_ADMIN'),
    (19, 'RO-001', 1, 'LAB'),
	(20, 'RO-002', 1, 'LAB'),
	(21, 'RO-003', 1, 'LAB')
    ;

INSERT INTO public.user_profile(
    email_address, employee_id, enabled, first_name, last_name, phone_number)
    VALUES ('developer@retrospecsoptometrists.co.zw', 'developerprince', true , 'Prince', 'Maposa', '+263786808538');

INSERT INTO public.application_user(
    user_id, encrypted_pass, security_code, email_address)
VALUES (1, '$2a$10$MhcCo3cqPYjB9ur3.By7yeUQMSYbjMooNIu75QsM5.Rke5sbwnTa6', 'XCT-IUYE-YYH', 'developer@retrospecsoptometrists.co.zw');


INSERT INTO public.userprofile_rolemap(
    email_address, role_map_id)
VALUES
('developer@retrospecsoptometrists.co.zw', 16),
('developer@retrospecsoptometrists.co.zw', 17),
('developer@retrospecsoptometrists.co.zw', 18);
