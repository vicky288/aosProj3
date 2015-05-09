netid=bxp131030
PROJDIR=$HOME/AOS/VotingProtocol
CONFIG=$PROJDIR/config.txt
PROG=Application

for line in `sed -n '/#Nodes/, /#Nodes/p' $CONFIG | sed '/^#.*/d'`
do
	echo $line
	nodeid=$( echo $line | cut -d ':' -f1)
	host1=$( echo $line | cut -d '@' -f1)
	host=$( echo $host1 | cut -d ':' -f2)
	echo $host
	echo $nodeid
	ssh -l "$netid" "$host.utdallas.edu" "cd $PROJDIR;java $PROG $nodeid" &
done
