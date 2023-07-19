
insert into SINGER (ID, FIRST_NAME, LAST_NAME, BIRTH_DATE) values (5, 'Nina', 'Simone', '1933-02-21');

insert into ALBUM (ID, SINGER_ID, TITLE, RELEASE_DATE) values (4, 5, 'Little Girl Blue', '1959-02-20');
insert into ALBUM (ID, SINGER_ID, TITLE, RELEASE_DATE) values (5, 5, 'Forbidden Fruit', '1961-08-18');
insert into ALBUM (ID, SINGER_ID, TITLE, RELEASE_DATE) values (6, 5, 'I Put a Spell on You', '1965-06-15');

insert into SINGER_INSTRUMENT(SINGER_ID, INSTRUMENT_ID) values (5, 'Voice');
insert into SINGER_INSTRUMENT(SINGER_ID, INSTRUMENT_ID) values (5, 'Piano');
