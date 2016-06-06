package tester_GUI;

public interface IControl {
	public void pressCola();
	public void pressFantom();
	public void pressStripe();
	public void pressAqua();
	public void cashReturn();
	public void insertTen();
	public void insertTwenty();
	public void insertFifty();
	public void insertHundred();
	public void insertTwoHundred();
	public java.lang.String showMessage();
	
	public void refillStripe(int q);
	public void refillCola(int q);
	public void refillFantom(int q);
	public void refillAqua(int q);
}