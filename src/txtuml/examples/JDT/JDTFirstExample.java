package txtuml.examples.JDT;

import txtuml.api.*;

class JDTFirstModel extends Model {
	
	class Employee extends ModelClass {
		String name = "anonymous";
		int id = 0;
		boolean ready = true;
		
		String getName() {
			return name;
		}
		
		int getId() {
			return id;
		}
		
		boolean isReady() {
			return ready;
		}
		
		void setName(String name) {
			this.name = name;
		}
		
		void setId(int id) {
			this.id = id;
		}
		
		void setIsReady(boolean ready) {
			this.ready = ready;
		}
		
		int work(int count) {
			if (!isReady()) {
				return -1;
			}
			ready = false;
			
			for (int i = 0; i < count; ++i) {
				log("Employee " + getName() + " with id " + getId() + " is now working.");
			}
			
			return count;
		}
		
		int work2(int count) {
			if (work(0) == -1) {
				return -1;
			}
			
			int i = 0;
			while (i < count) {
				log("Employee " + getName() + " with id " + getId() + " is now working but he/she is lazy.");
				i = i + 2;
			}

			return count;
		}
	}
	
	void test() {
		Employee emp = new Employee();
		emp.work(4);
		emp.setIsReady(true);
		emp.work2(6);
	}
}

public class JDTFirstExample {

	public static void main(String[] args) {
		new JDTFirstModel().test();
	}

}
