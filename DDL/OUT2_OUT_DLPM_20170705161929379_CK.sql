--------------------------CREATE TABLE dbo.QuestionnaireLogic2015-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIRELOGIC2015_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIRELOGIC2015_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NULL,
 LOGICNAME varchar(100) CHARACTER SET UNICODE  NULL,
 LOGICVALUE varchar(500) CHARACTER SET UNICODE  NULL,
 LOGICANSWERNAIRID varchar(1234) CHARACTER SET UNICODE  NULL,
 CREATETIME timestamp(6)   NULL,
 UPDATETIME timestamp(6)   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
LOGICVALUE
)
--------------------------CREATE TABLE dbo.QuestionnaireLogic2016-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIRELOGIC2016_H_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIRELOGIC2016_H_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NULL,
 LOGICNAME varchar(100) CHARACTER SET UNICODE  NULL,
 LOGICVALUE varchar(500) CHARACTER SET UNICODE  NULL,
 LOGICANSWERNAIRID varchar(1234) CHARACTER SET UNICODE  NULL,
 CREATETIME timestamp(6)   NULL,
 UPDATETIME timestamp(6)   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
ID,CREATETIME
)
--------------------------CREATE TABLE dbo.QuestionnaireRandItem2016-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIRERANDITEM2016_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIRERANDITEM2016_CK 
 (ID bigint   NOT NULL,
 GROUPID bigint   NULL,
 QUESTIONNAIREID bigint   NULL,
 QUESTIONID bigint   NULL,
 QLABEL varchar(50) CHARACTER SET UNICODE  NULL,
 QNAME varchar(50) CHARACTER SET UNICODE  NULL,
 QORDERNUMBER int   NULL,
 SHOWFLAG smallint   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
QUESTIONID
)
--------------------------CREATE TABLE dbo.QuestionnaireProperty2016-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIREPROPERTY2016_H_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIREPROPERTY2016_H_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NOT NULL,
 QUESTIONNAIRETITLE varchar(100) CHARACTER SET UNICODE  NOT NULL,
 ISHIDETITLE smallint   NULL,
 STARTEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 FINISHEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 DISCRIMINATEEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 QUOTAEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 TEMPORARILYCLOSED varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEHEADER varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEFOOTER varchar(1234) CHARACTER SET UNICODE  NULL,
 UPDATEDATE timestamp(6)   NOT NULL,
 UPDATEUSER varchar(50) CHARACTER SET UNICODE  NOT NULL,
 STATE smallint   NULL,
 VISITCODE varchar(1234) CHARACTER SET UNICODE  NULL,
 REPEAT_OG varchar(1234) CHARACTER SET UNICODE  NULL,
 ERRORMESSAGE varchar(1234) CHARACTER SET UNICODE  NULL,
 ISINTEGER varchar(1234) CHARACTER SET UNICODE  NULL,
 ISEMAIL varchar(1234) CHARACTER SET UNICODE  NULL,
 ISSKIPINTRO smallint   NULL,
 ISSHOWTITLE smallint   NULL,
 ISCLOSEEND smallint   NULL,
 ISEND_MESSAGE smallint   NULL,
 ISSTUDY_ONCE smallint   NULL,
 ISSCO_QUOTA smallint   NULL,
 ISPREVB_NODELETE smallint   NULL,
 ISPRINT_PREVIEW smallint   NULL,
 ISSHOW_BUTTON_ALT smallint   NULL,
 ISDATE varchar(1234) CHARACTER SET UNICODE  NULL,
 ISTIME varchar(1234) CHARACTER SET UNICODE  NULL,
 BUTTONSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 BARSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 TEXTSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 QUESTIONCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 ANSWERCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEHEADCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 TABLECOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 BORDERCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 ANSWERSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 JOINSETTING int   NULL,
 PROCESSTITLE varchar(50) CHARACTER SET UNICODE  NULL,
 ALLOWQUOTA smallint   NULL,
 FREQUENCE varchar(500) CHARACTER SET UNICODE  NULL,
 CHECKMODE smallint   NULL,
 CHECKBOXMODE varchar(100) CHARACTER SET UNICODE  NULL,
 RANDQUESTION smallint   NULL,
 TIMELIMIT int   NULL,
 ISFLOAT varchar(200) CHARACTER SET UNICODE  NULL,
 ISPHONE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMOBILE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMAXVALUE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMINVALUE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMAXLENGTH varchar(200) CHARACTER SET UNICODE  NULL,
 ISMINLENGTH varchar(200) CHARACTER SET UNICODE  NULL,
 ALLOWANOTHER smallint   NULL,
 ALLOWANOTHERTYPE int   NULL,
 ISSONQUESTIONNAIRE smallint   NULL,
 PARENTQUESTIONNAIREID bigint   NULL,
 LABELSTYLE varchar(100) CHARACTER SET UNICODE  NULL,
 SAMPLEFREQUENCE varchar(300) CHARACTER SET UNICODE  NULL,
 LIMITMESSAGE varchar(300) CHARACTER SET UNICODE  NULL,
 QUESTIONSSPACE int   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
PAGEFOOTER,ISPRINT_PREVIEW
)
--------------------------CREATE TABLE dbo.QuestionnaireSampler2016-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIRESAMPLER2016_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIRESAMPLER2016_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NOT NULL,
 SAMPLENAME varchar(100) CHARACTER SET UNICODE  NOT NULL,
 BACKURL varchar(500) CHARACTER SET UNICODE  NULL,
 URLAMOUNT int   NOT NULL,
 STATE smallint   NULL,
 CREATOR varchar(50) CHARACTER SET UNICODE  NOT NULL,
 CREATETIME timestamp(6)   NOT NULL,
 COMPLETESTATE varchar(100) CHARACTER SET UNICODE  NULL,
 URLTYPE int   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
STATE
)
--------------------------CREATE TABLE dbo.QuestionnaireProperty2015-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIREPROPERTY2015_H_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIREPROPERTY2015_H_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NOT NULL,
 QUESTIONNAIRETITLE varchar(100) CHARACTER SET UNICODE  NOT NULL,
 ISHIDETITLE smallint   NULL,
 STARTEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 FINISHEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 DISCRIMINATEEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 QUOTAEXPLANATION varchar(1234) CHARACTER SET UNICODE  NULL,
 TEMPORARILYCLOSED varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEHEADER varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEFOOTER varchar(1234) CHARACTER SET UNICODE  NULL,
 UPDATEDATE timestamp(6)   NOT NULL,
 UPDATEUSER varchar(50) CHARACTER SET UNICODE  NOT NULL,
 STATE smallint   NULL,
 VISITCODE varchar(1234) CHARACTER SET UNICODE  NULL,
 REPEAT_OG varchar(1234) CHARACTER SET UNICODE  NULL,
 ERRORMESSAGE varchar(1234) CHARACTER SET UNICODE  NULL,
 ISINTEGER varchar(1234) CHARACTER SET UNICODE  NULL,
 ISEMAIL varchar(1234) CHARACTER SET UNICODE  NULL,
 ISSKIPINTRO smallint   NULL,
 ISSHOWTITLE smallint   NULL,
 ISCLOSEEND smallint   NULL,
 ISEND_MESSAGE smallint   NULL,
 ISSTUDY_ONCE smallint   NULL,
 ISSCO_QUOTA smallint   NULL,
 ISPREVB_NODELETE smallint   NULL,
 ISPRINT_PREVIEW smallint   NULL,
 ISSHOW_BUTTON_ALT smallint   NULL,
 ISDATE varchar(1234) CHARACTER SET UNICODE  NULL,
 ISTIME varchar(1234) CHARACTER SET UNICODE  NULL,
 BUTTONSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 BARSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 TEXTSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 QUESTIONCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 ANSWERCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 PAGEHEADCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 TABLECOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 BORDERCOLOR varchar(1234) CHARACTER SET UNICODE  NULL,
 ANSWERSTYLE varchar(1234) CHARACTER SET UNICODE  NULL,
 JOINSETTING int   NULL,
 PROCESSTITLE varchar(50) CHARACTER SET UNICODE  NULL,
 ALLOWQUOTA smallint   NULL,
 FREQUENCE varchar(500) CHARACTER SET UNICODE  NULL,
 CHECKMODE smallint   NULL,
 CHECKBOXMODE varchar(100) CHARACTER SET UNICODE  NULL,
 RANDQUESTION smallint   NULL,
 TIMELIMIT int   NULL,
 ISFLOAT varchar(200) CHARACTER SET UNICODE  NULL,
 ISPHONE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMOBILE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMAXVALUE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMINVALUE varchar(200) CHARACTER SET UNICODE  NULL,
 ISMAXLENGTH varchar(200) CHARACTER SET UNICODE  NULL,
 ISMINLENGTH varchar(200) CHARACTER SET UNICODE  NULL,
 ALLOWANOTHER smallint   NULL,
 ALLOWANOTHERTYPE int   NULL,
 ISSONQUESTIONNAIRE smallint   NULL,
 PARENTQUESTIONNAIREID bigint   NULL,
 LABELSTYLE varchar(100) CHARACTER SET UNICODE  NULL,
 SAMPLEFREQUENCE varchar(300) CHARACTER SET UNICODE  NULL,
 LIMITMESSAGE varchar(300) CHARACTER SET UNICODE  NULL,
 QUESTIONSSPACE int   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
DISCRIMINATEEXPLANATION,ISSKIPINTRO,BUTTONSTYLE,RANDQUESTION
)
--------------------------CREATE TABLE dbo.QuestionnaireEICMessage-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIREEICMESSAGE_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIREEICMESSAGE_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NULL,
 SMSID bigint   NULL,
 TITLE_OG varchar(200) CHARACTER SET UNICODE  NULL,
 CARD varchar(50) CHARACTER SET UNICODE  NULL,
 PWD varchar(100) CHARACTER SET UNICODE  NULL,
 URL varchar(200) CHARACTER SET UNICODE  NULL,
 STATE int   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
SMSID
)
--------------------------CREATE TABLE dbo.ResourceInfo-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_RESOURCEINFO_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_RESOURCEINFO_CK 
 (ID bigint   NOT NULL,
 CATEGORYID bigint   NOT NULL,
 PARTNERCODE varchar(100) CHARACTER SET UNICODE  NOT NULL,
 TITLE_OG varchar(100) CHARACTER SET UNICODE  NOT NULL,
 KEYWORDS varchar(500) CHARACTER SET UNICODE  NULL,
 FILENAME varchar(200) CHARACTER SET UNICODE  NOT NULL,
 FILEEXT varchar(50) CHARACTER SET UNICODE  NOT NULL,
 FILEPATH varchar(200) CHARACTER SET UNICODE  NULL,
 CREATOR varchar(50) CHARACTER SET UNICODE  NOT NULL,
 CREATETIME timestamp(6)   NOT NULL,
 STATE smallint   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
ID,FILENAME
)
--------------------------CREATE TABLE dbo.QuestionInfo2015-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONINFO2015_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONINFO2015_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NOT NULL,
 NAME_OG varchar(20) CHARACTER SET UNICODE  NOT NULL,
 LABLE varchar(20) CHARACTER SET UNICODE  NOT NULL,
 TITLE_OG varchar(1234) CHARACTER SET UNICODE  NULL,
 QUESTIONTYPE varchar(10) CHARACTER SET UNICODE  NOT NULL,
 QORDERNUMBER int   NOT NULL,
 COLUMNSID varchar(20) CHARACTER SET UNICODE  NULL,
 SAMEPAGE smallint   NULL,
 ENDSAMEPAGE smallint   NULL,
 CREATOR varchar(50) CHARACTER SET UNICODE  NULL,
 CREATETIME timestamp(6)   NOT NULL,
 STATE smallint   NULL,
 GROUPSID varchar(20) CHARACTER SET UNICODE  NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
QORDERNUMBER
)
--------------------------CREATE TABLE dbo.QuestionnaireSummarize-------------------------------------
DROP TABLE ODS_DATA.O_DLPM_QUESTIONNAIRESUMMARIZE_H_CK ;
CREATE MULTISET TABLE ODS_DATA.O_DLPM_QUESTIONNAIRESUMMARIZE_H_CK 
 (ID bigint   NOT NULL,
 QUESTIONNAIREID bigint   NULL,
 TITLE_OG varchar(200) CHARACTER SET UNICODE  NULL,
 SUMMARIZE varchar(4000) CHARACTER SET UNICODE  NULL,
 PARTNERCODE varchar(50) CHARACTER SET UNICODE  NULL,
 CREATOR varchar(50) CHARACTER SET UNICODE  NULL,
 ISPUBLIC smallint   NULL,
 CREATETIME timestamp(6)   NULL,
 IN_ID BIGINT  NULL ,
 MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL ,
 IN_MD5ALL VARCHAR(50) CHARACTER SET UNICODE  NULL )
 PRIMARY INDEX (
TITLE_OG
)
