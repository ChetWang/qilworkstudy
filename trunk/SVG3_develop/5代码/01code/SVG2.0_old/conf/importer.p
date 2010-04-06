#这个配置文件只适合外部应用程序（非svg编辑器）对svg数据库中有blob字段的表进行导入导出

destinationDBServerIP=127.0.0.1
destinationDBServerPort=1521
destinationDBSID=qil
destinationDBUserName=svgtool
destinationDBUserPass=svgtool




#图元导入设置，图元从symbol.xml拆分导入
#######################################
#嘉兴的图元
singleSymbolXML=D:/nci/svg_hn_old/symbol_testDelete.xml
#海宁的图元
hnSymbolXML=D:/nci/svg_hn_old/hnSymbolContains.svg


#缓存的大symbol svg文件，必须要以.svg为后缀，最好现在指定磁盘位置上建立一个空的svg file
newSymbolSVG=D:/nci/svg_hn_old/newsymbol.svg

#转换的单个图元的缓存存放目录，注意，是一个必须存在的目录
convertedSymbolDir=D:/nci/svg_hn_old/convertedSymbols/