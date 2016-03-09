package pendulum;


import javax.media.j3d.BranchGroup;

public class MutableNode {
	private BranchGroup viewerNode;
	private BranchGroup rootNode;
	
	public MutableNode() {
		initViewerNode();
		initRootNode();
		viewerNode.addChild(rootNode);
	}
	
	public void add(BranchGroup group) {
		rootNode.addChild(group);
	}
		
	public void clear() {
		rootNode.detach();
		initRootNode();
		viewerNode.addChild(rootNode);
	}
 	
	public BranchGroup getNode() {
		return viewerNode;
	}
	
	private void initRootNode() {
		rootNode = new BranchGroup();
		rootNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		rootNode.setCapability(BranchGroup.ALLOW_DETACH);
	}
	
	private void initViewerNode() {
		viewerNode = new BranchGroup();
		viewerNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		viewerNode.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
	}
}
