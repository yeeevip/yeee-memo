pragma solidity ^0.4.20;
// We have to specify which version of compiler this code will compile with,
// version should be lower than (or equals to) the supported version showing on this tool.
contract Voting {
    /* Solidity doesn't let you pass in an array of strings in the constructor (yet).
    We will use an array of bytes32 instead to store the list of candidates with string type.
    */
    bytes32[] public candidateList;

    /* mapping field below is equivalent to an associative array or hash.
    The key of the mapping is candidate name stored as type bytes32 and value is
    an unsigned integer to store the vote count.
    */
    mapping (bytes32 => uint8) public votesReceived;

    /* Counting for candidate counts and total votes for all candidates
    */
    uint256 public candidateCount;
    uint256 public totalVotes;


    /* Events which will help for logging and debugging
    'identity' is like 'address' in original Solidity, length of 'identity' is 256 (bits).
    */
    event VOTE(bytes32 candidate, identity voterId);
    event VALID(bool valid);


    /* This is the constructor which will be called once when you
    deploy the contract to the blockchain. When we deploy the contract,
    we will pass an array of candidates who will be contesting in the election.
    You can input a candidate name like: Mike , it will be converted to bytes32.
    */
    constructor(bytes32[] candidateNames) public {
        candidateList = candidateNames;
        candidateCount = candidateList.length;
    }

    // This function returns the total votes a candidate has received so far.
    function totalVotesFor(bytes32 candidate) view public returns (uint8) {
        require(validCandidate(candidate), "candidate is invalid");
        return votesReceived[candidate];
    }

    // This function increments the vote count for the specified candidate.
    // This is equivalent to casting a vote.
    function voteForCandidate(bytes32 candidate) public {
        require(validCandidate(candidate), "candidate is invalid");
        votesReceived[candidate] += 1;
        totalVotes += 1;

        emit VOTE(candidate, msg.sender);
    }

    // This function will help to check whether target candidate is in the candidateList.
    function validCandidate(bytes32 candidate) view public returns (bool) {
        for (uint i = 0; i < candidateList.length; i++) {
            if (candidateList[i] == candidate) {
                emit VALID(true);
                return true;
            }
        }
        emit VALID(false);
        return false;
    }
}
