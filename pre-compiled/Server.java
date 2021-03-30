public class Server{
    public int coreCount;
		private int disk;
		private int ram;
		int id;
    		public Server
			(
				int c,
				int rm,
				int id
				)
			 {
			this.coreCount = c;
			this.ram = rm;
			this.id = id;
	
	}
	public int getCoreCOunt(){
		return this.coreCount;
	}
}