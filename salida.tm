0:       LD       6,0(0)
1:       ST       0,0(0)
2:       LDC       0,5(0)
3:       ST       0,0(6)
4:       LDC       0,5(0)
5:       ST       0,-1(6)
6:       LDC       0,1(0)
7:       LD       1,-1(6)
8:       MUL       0,1,0
9:       LD       1,0(6)
10:       SUB       0,1,0
11:       JEQ       0,2(7)
12:       LDC       0,0(0)
13:       LDA       7,1(7)
14:       LDC       0,1(0)
15:       ST       0,0(6)
16:       LDC       0,0(0)
17:       ST       0,-1(6)
18:       LDC       0,1(0)
19:       ST       0,-2(6)
20:       LDC       0,0(0)
21:       SUB       0,1,0
22:       JEQ       0,2(7)
23:       LDC       0,0(0)
24:       LDA       7,1(7)
25:       LDC       0,1(0)
26:       LD       1,-2(6)
27:       SUB       0,1,0
28:       JEQ       0,2(7)
29:       LDC       0,0(0)
30:       LDA       7,1(7)
31:       LDC       0,1(0)
32:       LD       1,-1(6)
33:       ADD       0,1,0
34:       JNE       0,2(7)
35:       LDC       0,0(0)
36:       LDA       7,1(7)
37:       LDC       0,1(0)
38:       LD       1,0(6)
39:       ADD       0,1,0
40:       JNE       0,2(7)
41:       LDC       0,0(0)
42:       LDA       7,1(7)
43:       LDC       0,1(0)
44:       ST       0,0(5)
45:       LD       0,0(5)
47:       LDC       0,1(0)
49:       LDC       0,77(0)
50:       OUT       0,0,0
48:       JEQ       0,3(7)
52:       LDC       0,5(0)
53:       OUT       0,0,0
51:       LDA       7,2(7)
46:       JEQ       0,8(7)
55:       HALT       0,0,0
