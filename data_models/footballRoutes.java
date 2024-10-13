public class RouteData {
	// class that is equivalent to the datamodel in mongoDB for football routes
	private List<Double> time;
	private List<Integer> xCoordinates;
	private List<Integer> yCoordinates;
	private String description;
	private String additionalInformation;
	
	public List<Double> getTime(){
		return time;
	}
	public void setTime(List<Double> time){
		this.time = time;
	}
	public List<Integer> getXCoordinates(){
		return xCoordinates;
	}
	public void setXCoordinates(List<Integer> xCoordinates){
		this.xCoordinates = xCoordinates;
	}
	public List<Integer> getYCoordinates(){
		return yCoordinates;
	}
	public void setYCoordinates(List<Integer> yCoordinates){
		this.yCoordinates = yCoordinates;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getAdditionalInformation(){
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation){
		this.additionalInformation = additionalInformation;
	}
}
