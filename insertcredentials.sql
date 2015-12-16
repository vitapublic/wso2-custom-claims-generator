insert into iam.credential (CREDENTIAL_NAME,BYU_ID,CREDENTIAL_TYPE,DATE_TIME_UPDATED,UPDATED_BY_ID,DATE_TIME_CREATED, CREATED_BY_ID,LOST_OR_STOLEN,STATUS,EXPIRATION_DATE,ISSUING_LOCATION,PHYSICAL_FORM,ASSOCIATED_DEVICE)
values ('2hZhnFK3i8dtYaV3xE_RaXE_o0Ma' /* Application consumer key */
   ,(select byu_id from pro.person where net_id = 'bdm4')
   ,'WSO2_CLIENT_ID',sysdate,'045325744',sysdate,'045325744','N',' ',null,' ',null,null);

/* stage key */   
insert into iam.credential (CREDENTIAL_NAME,BYU_ID,CREDENTIAL_TYPE,DATE_TIME_UPDATED,UPDATED_BY_ID,DATE_TIME_CREATED, CREATED_BY_ID,LOST_OR_STOLEN,STATUS,EXPIRATION_DATE,ISSUING_LOCATION,PHYSICAL_FORM,ASSOCIATED_DEVICE)
values ('2hZhnFK3i8dtYaV3xE_RaXE_o0Ma' /* Application consumer key */
   ,'785626940'
   ,'WSO2_CLIENT_ID',sysdate,'045325744',sysdate,'045325744','N',' ',null,' ',null,null);

delete iam.credential where credential_name = '2hZhnFK3i8dtYaV3xE_RaXE_o0Ma';
   
   
/* local key */   
insert into iam.credential (CREDENTIAL_NAME,BYU_ID,CREDENTIAL_TYPE,DATE_TIME_UPDATED,UPDATED_BY_ID,DATE_TIME_CREATED, CREATED_BY_ID,LOST_OR_STOLEN,STATUS,EXPIRATION_DATE,ISSUING_LOCATION,PHYSICAL_FORM,ASSOCIATED_DEVICE)
values ('7nrmqN0f0vrmlvy3aCMpAyEoxfAa' /* Application consumer key */
   ,'785626940'
   ,'WSO2_CLIENT_ID',sysdate,'045325744',sysdate,'045325744','N',' ',null,' ',null,null);
   


   select * from pro.person where byu_id = '785626940';
   
   select * from iam.credential where credential_type = 'WSO2_CLIENT_ID';
   
   delete iam.credential where credential_name = 'Dq0Px33idlSfNOg2hZyssU28O2Qa';
   
   select * from pro.person p, iam.credential c where p.byu_id = c.byu_id 
                    and c.credential_type = 'WSO2_CLIENT_ID' and c.credential_name = '7nrmqN0f0vrmlvy3aCMpAyEoxfAa';
                    
   select * from pro.person p, iam.credential c where p.byu_id = c.byu_id and c.credential_type = 'WSO2_CLIENT_ID' and c.credential_name = '7nrmqN0f0vrmlvy3aCMpAyEoxfAa';           