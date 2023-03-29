import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Barang {
    private int id;
    private String name;
    private int price;
    private int stock;
    private int sold;

    public Barang(int id, String name, int price, int stock, int sold) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sold = sold;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getSold() {
        return sold;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}

class AVLNode {
    public Barang barang;
    public AVLNode left;
    public AVLNode right;
    public int height;

    public AVLNode(Barang barang) {
        this.barang = barang;
        this.height = 1;
    }
}

class AVLTree {
    private LinkedList<AVLNode> nodeList = new LinkedList<>();

    private AVLNode root;

    private int getHeight(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    private int getBalanceFactor(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode newRoot = node.left;
        AVLNode subtree = newRoot.right;

        newRoot.right = node;
        node.left = subtree;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;

        return newRoot;
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode newRoot = node.right;
        AVLNode subtree = newRoot.left;

        newRoot.left = node;
        node.right = subtree;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;

        return newRoot;
    }

    private AVLNode insert(AVLNode node, Barang barang) {
        if (node == null) {
            AVLNode newNode = new AVLNode(barang);
            nodeList.add(newNode);
            return newNode;
        }

        if (barang.getId() < node.barang.getId()) {
            node.left = insert(node.left, barang);
        } else if (barang.getId() > node.barang.getId()) {
            node.right = insert(node.right, barang);
        } else {
            return node;
        }

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1 && barang.getId() < node.left.barang.getId()) {
            return rotateRight(node);
        }

        if (balanceFactor < -1 && barang.getId() > node.right.barang.getId()) {
            return rotateLeft(node);
        }
        if (balanceFactor > 1 && barang.getId() > node.left.barang.getId()) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balanceFactor < -1 && barang.getId() < node.right.barang.getId()) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void insert(Barang barang) {
        root = insert(root, barang);
    }

    public void delete(int id) {
        AVLNode node = search(id);

        if (node != null) {
            nodeList.remove(node);
            root = delete(root, id);
        }
    }

    private AVLNode delete(AVLNode node, int id) {
        if (node == null) {
            return node;
        }

        if (id < node.barang.getId()) {
            node.left = delete(node.left, id);
        } else if (id > node.barang.getId()) {
            node.right = delete(node.right, id);
        } else {
            if (node.left == null || node.right == null) {
                AVLNode temp = null;

                if (temp == node.left) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                AVLNode temp = getMinNode(node.right);
                node.barang = temp.barang;
                node.right = delete(node.right, temp.barang.getId());
            }
        }

        if (node == null) {
            return node;
        }

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode getMinNode(AVLNode node) {
        AVLNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    public AVLNode search(int id) {
        AVLNode node = root;

        while (node != null) {
            if (id == node.barang.getId()) {
                return node;
            } else if (id < node.barang.getId()) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return null;
    }

    public void search(String name) {
        Boolean found = false;
        for (AVLNode node : nodeList) {
            if (node.barang.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(
                        node.barang.getName() + " " + node.barang.getPrice() + " " + node.barang.getStock() + " "
                                + node.barang.getSold());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Barang tidak ditemukan");
        }
    }

    public void displayAll() {
        System.out.println("ID \tNama \tHarga \tStok \tTerjual");
        for (AVLNode node : nodeList) {
            System.out.println(node.barang.getId() + " " + node.barang.getName() + " " + node.barang.getPrice() + " "
                    + node.barang.getStock() + " " + node.barang.getSold());
        }
    }

    public void sortByPrice() {
        Collections.sort(nodeList, new Comparator<AVLNode>() {
            @Override
            public int compare(AVLNode o1, AVLNode o2) {
                return o1.barang.getPrice() - o2.barang.getPrice();
            }
        });
    }

    public void sortByStock() {
        Collections.sort(nodeList, new Comparator<AVLNode>() {
            @Override
            public int compare(AVLNode o1, AVLNode o2) {
                return o1.barang.getStock() - o2.barang.getStock();
            }
        });
    }

    public void displayBarang(int id) {
        AVLNode node = search(id);
        if (node != null) {
            System.out.println("ID \tNama \tHarga \tStok \tTerjual");
            System.out.println(node.barang.getId() + " " + node.barang.getName() + " " + node.barang.getPrice() + " "
                    + node.barang.getStock() + " " + node.barang.getSold());
        } else {
            System.out.println("Barang tidak ditemukan");
        }
    }

    public void updateBarang(int id, int stock, int sold) {
        AVLNode node = search(id);
        if (node != null) {
            node.barang.setStock(stock);
            node.barang.setSold(sold);
            System.out.println("Barang berhasil diupdate");
        } else {
            System.out.println("Barang tidak ditemukan");
        }
    }

    public void getTotalStock() {
        int total = 0;
        for (AVLNode node : nodeList) {
            total += node.barang.getStock();
        }
        System.out.println("Total stok barang: " + total);
    }

    public void sellBarang(int id, int amount) {
        AVLNode node = search(id);
        if (node != null) {
            if (node.barang.getStock() >= amount) {
                node.barang.setStock(node.barang.getStock() - amount);
                node.barang.setSold(node.barang.getSold() + amount);
                System.out.println("Barang berhasil terjual sejumlah " + amount + " buah, untuk barang "
                        + node.barang.getName() + "");
            } else {
                System.out.println("Stok tidak mencukupi");
            }
        } else {
            System.out.println("Barang tidak ditemukan");
        }
    }

    public void restockBarang(int id, int amount) {
        AVLNode node = search(id);
        if (node != null) {
            node.barang.setStock(node.barang.getStock() + amount);
            System.out.println("Barang berhasil direstock sejumlah " + amount + " buah, untuk barang "
                    + node.barang.getName() + "");
        } else {
            System.out.println("Barang tidak ditemukan");
        }
    }

}

public class App {
    public static void main(String[] args) {

        AVLTree tree = new AVLTree();
        try {
            FileInputStream fileInputStream = new FileInputStream("data.txt");
            Scanner scanner = new Scanner(fileInputStream);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int price = Integer.parseInt(data[2]);
                int stock = Integer.parseInt(data[3]);
                int sold = Integer.parseInt(data[4]);
                // int sold = 0;
                tree.insert(new Barang(id, name, price, stock, sold));
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner1 = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to Product Management System");
            System.out.println("1. Display all products");
            System.out.println("2. Create new product");
            System.out.println("3. Delete product");
            System.out.println("4. Sort products");
            System.out.println("5. Get product by ID");
            System.out.println("6. Update product by ID");
            System.out.println("7. Get total stock");
            System.out.println("8. Sell product");
            System.out.println("9. Restock product");
            System.out.println("0. Exit");
            System.out.print("Please enter your choice: ");
            int choice = scanner1.nextInt();

            switch (choice) {
                case 1:
                    tree.displayAll();
                    break;
                case 2:
                    System.out.println("Enter product name: ");
                    String name = scanner1.next();
                    System.out.println("Enter product price: ");
                    int price = scanner1.nextInt();
                    System.out.println("Enter product stock: ");
                    int stock = scanner1.nextInt();
                    Random random = new Random();
                    int id = random.nextInt(100000);
                    tree.insert(new Barang(id, name, price, stock, 0));
                    break;
                case 3:
                    System.out.println("Enter product ID: ");
                    int id1 = scanner1.nextInt();
                    tree.delete(id1);
                    break;
                case 4:
                    System.out.println("1. Sort by price");
                    System.out.println("2. Sort by stock");
                    System.out.print("Please enter your choice: ");
                    int choice1 = scanner1.nextInt();
                    switch (choice1) {
                        case 1:
                            tree.sortByPrice();
                            tree.displayAll();
                            break;
                        case 2:
                            tree.sortByStock();
                            tree.displayAll();
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Enter product ID: ");
                    int id2 = scanner1.nextInt();
                    tree.displayBarang(id2);
                    break;
                case 6:
                    System.out.println("Enter product ID: ");
                    int id3 = scanner1.nextInt();
                    System.out.println("Enter product stock: ");
                    int stock1 = scanner1.nextInt();
                    System.out.println("Enter product sold: ");
                    int sold = scanner1.nextInt();
                    tree.updateBarang(id3, stock1, sold);
                    break;
                case 7:
                    tree.getTotalStock();
                    break;
                case 8:
                    System.out.println("Enter product ID: ");
                    int id4 = scanner1.nextInt();
                    System.out.println("Enter product amount: ");
                    int amount = scanner1.nextInt();
                    tree.sellBarang(id4, amount);
                    break;
                case 9:
                    System.out.println("Enter product ID: ");
                    int id5 = scanner1.nextInt();
                    System.out.println("Enter product amount: ");
                    int amount1 = scanner1.nextInt();
                    tree.restockBarang(id5, amount1);
                    break;
                case 0:
                    System.out.println("Thank you for using our system!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }

    }
}
