schedulix
=========

schedulix is an open source enterprise job scheduling system.
Instructions for compilation and installation can be found in doc/installtion_en.pdf (English)
or doc/installation.pdf (German).

Documentation of the user interface can be found in doc/online_en.pdf (English) or
doc/online_de.pdf (German).

All the progress will be twittered (https://twitter.com/schedulix).

-------------------------------------------------------------------------------------------

Important notice:
Due to a changed read strategy of the code generated by jflex 1.6.0, we cannot recommend
to use this version of jflex at the moment.

-------------------------------------------------------------------------------------------

The last stable branch is v2.6
It can be obtained by doing a

git clone https://github.com/schedulix/schedulix.git -b v2.6

The previous stable branch is v2.5.1
It can be obtained by doing a

git clone https://github.com/schedulix/schedulix.git -b v2.5.1

-------------------------------------------------------------------------------------------

When upgrading from the stable 2.5.1 version to the v2.6 branch,
don't forget to upgrade the database schema (adapt these statements to the dialect of
the dbms used):

ALTER TABLE USERS
ADD COLUMN SALT VARCHAR(64) WITH NULL;

ALTER TABLE USERS
ADD COLUMN METHOD INTEGER NOT NULL WITH DEFAULT; /* 0 */

ALTER TABLE SCOPE
ADD COLUMN SALT VARCHAR(64) WITH NULL;

ALTER TABLE SCOPE
ADD COLUMN METHOD INTEGER NOT NULL WITH DEFAULT; /* 0 */

Now depending on your DBMS you'll have to find some method to change the definition
of the PASSWD column in the table SCOPE. It has length 40 which should be 64.
In case of Ingres, one has to go the long way, like:
CREATE TABLE X AS SELECT * FROM SCOPE;
DROP TABLE SCOPE;
\i sql/ing_gen/SCOPE.sql  /* \i stands for "include" */
INSERT INTO SCOPE SELECT * FROM X;
DROP TABLE X;
COMMIT;

ALTER TABLE RESOURCE_REQUIREMENT
ADD COLUMN STICKY_NAME VARCHAR(64) WITH NULL;

ALTER TABLE RESOURCE_REQUIREMENT
ADD COLUMN STICKY_PARENT DECIMAL(20) WITH NULL;

ALTER TABLE RESOURCE_ALLOCATION
ADD COLUMN STICKY_NAME VARCHAR(64) WITH NULL;

ALTER TABLE RESOURCE_ALLOCATION
ADD COLUMN STICKY_PARENT DECIMAL(20) WITH NULL;

-------------------------------------------------------------------------------------------

Happy Hacking :-)
