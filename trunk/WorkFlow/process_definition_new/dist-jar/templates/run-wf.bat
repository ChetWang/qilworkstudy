rem 新世纪工作流定义器应用程序
rem 使用的类库未经混淆
setlocal EnableDelayedExpansion
set WF_CLASS_PATH=%~dp0
FOR %%c in ("*.jar") DO set WF_CLASS_PATH=!WF_CLASS_PATH!;%%c
java -cp "%WF_CLASS_PATH%" com.nci.domino.main.MainJFrame