
CREATE FUNCTION IF NOT EXISTS getFirstNameById(in_id INT) RETURNS VARCHAR(60) LANGUAGE SQL RETURN (SELECT first_name FROM SINGER WHERE id = in_id);