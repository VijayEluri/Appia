@echo off

echo Starting group member 1
start java org.continuent.appia.demo.ExecuteXML config\demo\perf.xml
echo Starting group member 2
start java org.continuent.appia.demo.ExecuteXML config\demo\perf.xml
echo Test finished
