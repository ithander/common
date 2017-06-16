select
===
select * from user where 1=1
@if(!isEmpty(upass)){
and upass = #upass#
@}
@if(!isEmpty(uname)){
and uname = #uname#
@}