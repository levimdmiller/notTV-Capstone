const React = require("react");

/*
class Button extends React.Component{
	render(){
		return(
			<button className="btn btn-default" type="button" onClick={this.props.onClickFunction}> { (this.state.subscribed == true) ? "Unsubscribe" : "Subscribe" }</button>
		);
	}
}*/

export default class AuthorHeader extends React.Component {
	constructor(props) {
		super(props);
		
		this.state = {
			subscribed: false,
			sub: "Subscribe",
			unsub: "Unsubscribe"
		};

		this.subscribe = this.subscribe.bind(this);
		this.unsubscribe = this.unsubscribe.bind(this);
		this.checkForSubscribed = this.checkForSubscribed.bind(this);
		this.init = this.init.bind(this);
		
		this.init = this.init();
	}
	
	checkForSubscribed(loggedInUser, userFromProfile){
		console.log(loggedInUser);
		console.log(userFromProfile);
    	$.get({
    	    url: config.serverUrl + "/info/checkSubscribed",
    	    data: {
    			userID1: loggedInUser,
    			userID2: userFromProfile[0] // this is actually an array of 1 element
    		    },
    	    dataType: "json",
    	    success: (data) => {
    	    	console.log("Successfully checked if user is subscribed.");
    	    	this.setState({
    	    		subscribed: data
    	    	});
    	    },
    	    error: (response) => {
    	    	console.log("Failed to check if logged in user is subscribed");
    	    } 
    	});
    }
	
	init(){
		this.checkForSubscribed(this.props.loggedIn_userID, this.props.userID);
	}
	
	subscribe(loggedInUser, userFromProfile){
    	$.get({
    	    url: config.serverUrl + "/info/subscribe",
    	    data: {
    			userID1: loggedInUser,
    			userID2: userFromProfile[0] // this is actually an array of 1 element
    		    },
    	    dataType: "json",
    	    success: (data) => {
    	    	console.log("Successfully subscribed.");
    	    	if(data){	// ajax call actually returns a boolean
    	    		// check and update (if you have to) the subscription status
    	    		this.checkForSubscribed(loggedInUser, userFromProfile);
    	    	} else {
    	    		console.log("Could not subscribe for some reason.");
    	    	}
    	    },
    	    error: (response) => {
    		console.log("Unsuccessful subscribe!");
    	    }
    	});
    }
	
	
	unsubscribe(loggedInUser, userFromProfile){
    	$.get({
    	    url: config.serverUrl + "/info/unsubscribe",
    	    data: {
    			userID1: loggedInUser,
    			userID2: userFromProfile[0] // this is actually an array of 1 element
    		    },
    	    dataType: "json",
    	    success: (data) => {	
	    		console.log("Successfully unsubscribed.");
    	    	if(data){	// ajax call actually returns a boolean
    	    		// check and update (if you have to) the subscribe status
    	    		this.checkForSubscribed(loggedInUser, userFromProfile);
    	    	} else {
    	    		console.log("Could not unsubscribe for some reason.");
    	    	}
    	    },
    	    error: (response) => {
    		console.log("Unsuccessful unsubscribe.");
    	    }
    	});
    }
	
	render() {
		return (
				<div className="media">
					<div className="media-left">
						<img src="../img/user.png"></img>
					</div>
					<div className="media-body">
						<h4 className="media-heading">{this.props.username}</h4>
						<p>{this.props.description}</p>
						<div>
							<button className="btn btn-default" type="button" onClick={ (this.state.subscribed) ? (() => this.unsubscribe(this.props.loggedIn_userID, this.props.userID)) : (() => this.subscribe(this.props.loggedIn_userID, this.props.userID)) }> { (this.state.subscribed) ? this.state.unsub : this.state.sub } </button> 
						</div>
					</div>
				</div>
		);
	}
}


/* 
 * <div>{
							(this.state.subscribed == true) ? <Button onClickFunction = {() => this.unsubscribe(this.props.loggedIn_userID, this.props.userID)} /> : 
								<Button onClickFunction={() => this.subscribe(this.props.loggedIn_userID, this.props.userID)} />
						}</div> 
 */

