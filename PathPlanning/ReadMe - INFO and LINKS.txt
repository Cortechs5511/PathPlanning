Links to CD threads and other info:

https://www.chiefdelphi.com/forums/showthread.php?t=130769

https://www.chiefdelphi.com/forums/showthread.php?t=136798

ORIGINAL CODE from Ty’s blog post, before mods:
https://github.com/KHEngineering/SmoothPathPlanner


General Idea:
Decide on Path
Enter Path Points(and times)
Generate graph (tweak, etc…)
Export position data (L and R)
WinSCP / SSH into RoboRio and store position data file
Rio loads file at startup
Autonomous begin: path positions are fed into PID(F) controller
Profit!!




