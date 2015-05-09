#!/bin/bash


# Change this to your netid
netid=bxp131030

#
# Root directory of your project
PROJDIR=$HOME/AOS/VotingProtocol

#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#

CONFIG=$PROJDIR/config.txt

#
# Directory your java classes are in
#
BINDIR=$PROJDIR

#
# Your main project class
#
PROG=Application
isNodeInfo=0
#n=1

for line in `sed -n '/#Nodes/,/#Nodes/p' $CONFIG | sed '/^#.*/d'`
do
    	#echo $line
	hostport=$( echo $line | cut  -d ":" -f2)
	host=$( echo $hostport | cut -d "@" -f1)
	echo $host
	port=$( echo $hostport | cut -d "@" -f2 )
	echo $port
	echo "!!"

	ssh -l "$netid" "$host.utdallas.edu" "pkill -9 -u $netid" &
	#ssh -l "$netid" "dc44.utdallas.edu" "killall -u axg131530" &
done
