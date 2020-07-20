
export class Restaurant {

	name: string;

	constructor(nm: string) { 
		this.name = nm;
	}
}

export class Diner {
	name: string;
	email: string;
	
	constructor(nm: string, em: string) { 
		this.name = nm;
		this.email = em;
	}
}
