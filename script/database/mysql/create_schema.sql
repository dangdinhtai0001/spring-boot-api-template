DROP database TEMPLATE;
DROP domainUser 'template'@'localhost';

CREATE DATABASE TEMPLATE;
CREATE domainUser 'template'@'localhost' IDENTIFIED by 'Abc123456';

GRANT ALL ON TEMPLATE.* TO 'template'@'localhost';
GRANT FILE ON *.* TO 'template'@'localhost';
FLUSH PRIVILEGES;