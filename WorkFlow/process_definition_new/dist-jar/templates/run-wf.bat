rem �����͹�����������Ӧ�ó���
rem ʹ�õ����δ������
setlocal EnableDelayedExpansion
set WF_CLASS_PATH=%~dp0
FOR %%c in ("*.jar") DO set WF_CLASS_PATH=!WF_CLASS_PATH!;%%c
java -cp "%WF_CLASS_PATH%" com.nci.domino.main.MainJFrame