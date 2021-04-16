package org.springframework.cheapy.model;

public enum StatusOffer {
	active{
		@Override
		public String toString() {
			return "Activa";
			
		}
		
	}
	
	, inactive{
		@Override
		public String toString() {
			return "Inactiva";
			
		}
		
	}
	
	
	, hidden{
		@Override
		public String toString() {
			return "Oculta";
			
		}
		
	}
}
