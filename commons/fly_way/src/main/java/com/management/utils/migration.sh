# 自动生成sql文件名称的脚本
if [ -n "$1" ];then
  newSqlFile=commons/fly_way/src/main/resources/db/migration/V`date +%Y%m%d%H%M%S`__${1}.sql
  touch $newSqlFile
  echo "a new migration script generated at: "$newSqlFile
else
  echo "请输入迁移脚本名称"
fi