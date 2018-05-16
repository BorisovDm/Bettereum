pragma solidity ^0.4.22;


contract Ownable {

    address owner;

    constructor() public {
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    function transferOwnership(address newOwner) public onlyOwner {
        owner = newOwner;
    }
}


contract Bets is Ownable {

    uint currentId = 0;
    uint constant commission = 10;

    struct Bet {
        uint id;
        uint result;
        address client;
        uint summ;
    }

    mapping(uint => Bet) private bets;
    mapping(uint => uint) private balances;

    function newBet(uint result) public payable {
        currentId += 1;
        bets[currentId] = Bet(currentId, result, msg.sender, msg.value);
        balances[result] += msg.value;
    }

    function getResult(uint id) public onlyOwner constant returns (uint)  {
        return bets[id].result;
    }

    function getClient(uint id) public onlyOwner constant returns (address)  {
        return bets[id].client;
    }

    function getSumm(uint id) public onlyOwner constant returns (uint) {
        return bets[id].summ;
    }

    function getCurrentId() public constant returns (uint)  {
        return currentId;
    }

    function getBalance() public constant returns (uint) {
        return address(this).balance;
    }

    function finishBet(uint result) public onlyOwner {

        if (commission <= address(this).balance) {
            owner.transfer(commission);
        }

        uint coeff = address(this).balance / balances[result];

        for (uint i = 0; i < getCurrentId(); i++){
            Bet storage _bet = bets[i];
            if (_bet.result == result) {
                uint sumToTransfer = _bet.summ * coeff;

                if (sumToTransfer <= address(this).balance) {
                    _bet.client.transfer(_bet.summ * coeff);
                } else {
                    _bet.client.transfer(address(this).balance);
                    break;
                }
            }
        }

        kill();
    }

    function kill() private onlyOwner {
        selfdestruct(owner);
    }
}
