(function(){
	var app = angular.module('store',[]);
	
	app.controller('StoreController', function(){
		this.products = gems;
	});
	
	var gems = [
		{
			name: 'Decahedron',
			price: 2.5,
			description: 'hello',
			canPurchase: true,
			soldOut: false,
			images: [
				{full: '1.jpg'}
			]
		},
		{
			name: 'Pentagonal',
			price: 3.5,
			description: 'world',
			canPurchase: false,
			soldOut: false,
			images: [
				{full: '2.jpg'}
			]
		}
	];
})();

