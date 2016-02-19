
public class Pair<A, B>  {
	private A first;
	private B second;
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	
	public A getFirst() {
		return first;
	}
	
	public void setFirst(A first) {
		this.first = first;
	}
	
	public B getSecond() {
		return second;
	}
	
	public void setSecond(B second) {
		this.second = second;
	}
	
	public int hashCode() {
		int hashCodeFirst = first != null ? first.hashCode(): 0;
		int hashCodeSecond = second != null ? second.hashCode() : 0;
		
		return (hashCodeFirst + hashCodeSecond) * hashCodeSecond + hashCodeFirst;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Pair) {
			Pair objPair = (Pair) obj;
			return 
					((  this.first == objPair.first ||
	    			( this.first != null && objPair.first != null &&
	    			  this.first.equals(objPair.first))) &&
	    		 (	this.second == objPair.second ||
	    			( this.second != null && objPair.second != null &&
	    			  this.second.equals(objPair.second))) );
		}
		return false;
	}


	
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

}
