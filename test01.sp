MB1 nb1 nb1 vdd vdd cmosp w=15.3e-6 l=1.0e-6
RB1 nb1 nb2 8500
RB2 nb2 nb3 4800
RB3 nb3 nb4 10000
RB4 nb4 vss 6300
MI1 ni1 nb3 vdd vdd cmosp w=38.0e-6 l=1.0e-6
MI2 ni2 inm ni1 vdd cmosp w=0.5e-6 l=1.0e-6
MI3 ni3 inp ni1 vdd cmosp w=0.5e-6 l=1.0e-6
MI4 ni2 nb3 ni4 vss cmosn w=0.8e-6 l=1.0e-6
MI5 ni3 nb3 ni5 vss cmosn w=0.8e-6 l=1.0e-6
MI6 ni4 ni2 ni6 vss cmosn w=48.6e-6 l=1.0e-6
MI7 ni5 ni2 ni7 vss cmosn w=48.6e-6 l=1.0e-6
RI1 ni6 vss 8400
RI2 ni7 vss 8400
MO1 no1 nb1 vdd vdd cmosp w=44.9e-6 l=1.0e-6
MO2 out nb3 no1 no1 cmosp w=42.4e-6 l=1.0e-6
MO3 out nb3 no2 vss cmosn w=38.5e-6 l=1.0e-6
MO4 no2 ni3 vss vss cmosn w=34.6e-6 l=1.0e-6
RO1 no3 out 5000
CO1 no3 ni3 0.5e-12
