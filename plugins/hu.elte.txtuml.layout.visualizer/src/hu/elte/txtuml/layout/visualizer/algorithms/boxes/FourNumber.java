package hu.elte.txtuml.layout.visualizer.algorithms.boxes;

import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;

class FourNumber
{
	private Integer[] _num;
	
	public FourNumber(Integer decimals)
	{
		_num = new Integer[decimals + 1];
	}
	
	public void next() throws InternalException
	{
		_num[0] = _num[0] + 1;
		
		if (_num[0] > 3)
		{
			Integer flowOver = 0;
			for (int i = 0; i < _num.length; ++i)
			{
				_num[i] = _num[i] + flowOver;
				
				if (_num[i] > 3)
				{
					flowOver = _num[i] - 3;
					_num[i] = _num[i] - flowOver;
				}
				else
				{
					flowOver = 0;
				}
			}
			
			if (flowOver != 0)
				throw new InternalException("Overflow!");
		}
	}
	
	public boolean isLessThanOrEqual(FourNumber other) throws InternalException
	{
		if (_num.length != other._num.length)
			throw new InternalException("Cannot compare!");
		
		for (int i = _num.length - 1; i >= 0; --i)
		{
			if(_num[i] > other._num[i])
				return false;
		}
		
		return true;
	}
	
	public Integer getBit(Integer i)
	{
		return _num[i];
	}
	
	public static FourNumber Zero(Integer decimals)
	{
		FourNumber result = new FourNumber(decimals);
		
		for (int i = 0; i < result._num.length; ++i)
		{
			result._num[i] = 0;
		}
		
		return result;
	}
	
	public static FourNumber Max(Integer decimals)
	{
		FourNumber result = new FourNumber(decimals);
		
		for (int i = 0; i < decimals; ++i)
		{
			result._num[i] = 3;
		}
		result._num[decimals] = 0;
		
		return result;
	}

	public Integer decimalValue()
	{
		Integer result = 0;
		
		for(int i = 0; i < _num.length; ++i)
		{
			result += _num[i] * (int)Math.pow(4, i);
		}
		
		return result;
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		for (int i = _num.length - 1; i >= 0; --i)
		{
			result += _num[i].toString();
		}
		
		result += " = " + decimalValue().toString();
		
		return result;
	}


}
