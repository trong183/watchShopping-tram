package function;

import java.util.ArrayList;
import java.util.Scanner;

import management.UserManagement;
import management.WatchManagement;
import user.Customer;
import user.Staff;
import user.User;
import util.CheckInput;
import util.Constant;
import util.Menu;
import watch.Watches;

public class AdminFunction {
	static WatchManagement watchManagement = new WatchManagement(Constant.dataPath.watches_File);
	static UserManagement userManagement = new UserManagement(Constant.dataPath.accounts_File);

	public static boolean AdminManagement(Scanner scanner) {
		Menu.admin();

		String choice;
		System.out.print("Enter your choice: ");
		choice = scanner.nextLine();
		if (choice == null)
			return true;
		switch (choice) {
			case "1":
				Menu.product();
				productManagement(scanner);
				break;
			case "2":
				Menu.account();
				accountManagement(scanner);
				break;
			default:
				return true;
		}
		return false;
	}

	public static void productManagement(Scanner scanner) {
		// viewProduct(watchManagement);
		displayWatchlist(watchManagement);
		String choice;
		System.out.print("Enter your choice[1-4]: ");
		choice = scanner.nextLine();
		if (choice == null)
			return;
		switch (choice) {
			case "1":
				addProduct(watchManagement, scanner);
				break;
			case "2":
				editProduct(watchManagement, scanner);
				break;
			case "3":
				deleteProduct(watchManagement, scanner);
				break;
			case "4":
				searchProduct(watchManagement, scanner);
				break;
		}
	}

	public static void accountManagement(Scanner scanner) {
		displayUserlist(userManagement);
		String choice;
		System.out.print("Enter your choice[1-4]: ");
		choice = scanner.nextLine();
		if (choice == null)
			return;
		switch (choice) {
			case "1":
				deleteAccount(userManagement, scanner);
				break;
			case "2":
				searchAccount(userManagement, scanner);
				break;
			case "3":
				addStaff(userManagement, scanner);
				break;
			case "4":
				blockCustomer(userManagement, scanner);
				break;
		}
	}

	public static void viewProduct(WatchManagement watchManagement) {

		for (Object watch : watchManagement.getWatchList().getArr()) {
			System.out.println((Watches) watch);
		}
	}

	public static void addProduct(WatchManagement watchManagement, Scanner scanner) {
		String id;
		System.out.print("Enter id: ");
		while (true) {
			id = scanner.nextLine();
			if (watchManagement.getWatchList().findIndex(id) == -1) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		System.out.print("Enter name: ");
		String name = scanner.nextLine();
		System.out.print("Enter brand: ");
		String brand = scanner.nextLine();
		System.out.print("Enter type: ");
		String type = scanner.nextLine();
		System.out.print("Enter price: ");
		double price;
		while (true) {
			price = Double.parseDouble(scanner.nextLine());
			if (price > 0) {
				break;
			}
			System.out.print("Price must be greater than 0. Enter again: ");
		}
		System.out.print("Enter quantity: ");
		int quantity;
		while (true) {
			quantity = Integer.parseInt(scanner.nextLine());
			if (quantity > 0) {
				break;
			}
			System.out.print("Quantity must be greater than 0. Enter again: ");
		}
		watchManagement.addWatches(new Watches(id, name, brand, type, price, quantity));
	}

	public static void searchProduct(WatchManagement watchManagement, Scanner scanner) {
		System.out.print("Enter product name: ");
		String name = scanner.nextLine();
		ArrayList<Watches> list = watchManagement.searchWatchesByName(name);
		for (Watches watches : list) {
			System.out.println(watches);
		}
	}

	public static void deleteAccount(UserManagement userManagement, Scanner scanner) {
		displayUserlist(userManagement);
		String id;
		System.out.print("Enter id: ");
		while (true) {
			id = scanner.nextLine();
			if (userManagement.getAcclist().findIndex(id) != -1) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		userManagement.removeUser(id);

	}

	public static void searchAccount(UserManagement userManagement, Scanner scanner) {
		displayUserlist(userManagement);
		System.out.print("Enter Name: ");
		String name = scanner.nextLine();
		for (Object user : userManagement.getAcclist().getArr()) {
			if (((User) user).getFullName().contains(name)) {
				System.out.println((User) user);
			}
		}
	}

	public static void displayUserlist(UserManagement userManagement) {
		System.out.format("%-20s%-10s%-30s%-15s%-10s%-10s\n", "ID", "Password", "Fullname", "Role", "Status", "Shift");
		for (Object user : userManagement.getAcclist().getArr()) {
			if (user instanceof Customer) {
				System.out.format("%-20s%-10s%-30s%-15s%-10s%-10s\n",
						((User) user).getId(),
						((User) user).getPassword(),
						((User) user).getFullName(),
						((User) user).getRole(),
						((Customer) user).getStatus(),
						"no shifts");
			} else {
				System.out.format("%-20s%-10s%-30s%-15s%-10d%-10s\n",
						((User) user).getId(),
						((User) user).getPassword(),
						((User) user).getFullName(),
						((User) user).getRole(),
						5,
						String.valueOf(((Staff) user).getShift()));
			}
		}
	}

	public static void displayWatchlist(WatchManagement watchManagement) {
		System.out.format("%-10s%-20s%-20s%-20s%-20s%-20s\n", "ID", "Name", "Brand", "Type", "Price", "Quantity");
		for (Object watch : watchManagement.getWatchList().getArr()) {
			System.out.format("%-10s%-20s%-20s%-20s%-20f%-20d\n",
					((Watches) watch).getId(),
					((Watches) watch).getName(),
					((Watches) watch).getBrand(),
					((Watches) watch).getType(),
					((Watches) watch).getPrice(),
					((Watches) watch).getQuantity());

		}
	}

	public static void addStaff(UserManagement userManagement, Scanner scanner) {
		displayUserlist(userManagement);
		String id;
		System.out.print("Enter Id you want to .... staff ");
		// System.out.print("Enter id: ");
		while (true) {
			id = scanner.nextLine();
			if (userManagement.getAcclist().findIndex(id) != -1) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		int shift;
		do {
			System.out.println("Enter staff's shift [1-4]: ");
			shift = CheckInput.toIntNumeric(scanner.nextLine(), 1, 4);
		} while (shift == -1);
		int index = userManagement.getAcclist().findIndex(id);
		User tmp = (User) userManagement.getAcclist().get(index);
		userManagement.getAcclist().set(
				index,
				new Staff(
						tmp.getId(),
						tmp.getPassword(),
						tmp.getFullName(),
						"Staff",
						shift));
		userManagement.writeFile();
	}

	public static void blockCustomer(UserManagement userManagement, Scanner scanner) {
		displayUserlist(userManagement);
		System.out.print("Enter id: ");
		String id;
		int index;
		while (true) {
			id = scanner.nextLine();
			index = userManagement.getAcclist().findIndex(id);
			if (index != -1 &&
					((User) userManagement.getAcclist().get(index)).getRole().equals("customer")) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		User tmp = (User) userManagement.getAcclist().get(index);
		userManagement.getAcclist().set(index, new Customer(tmp.getId(), tmp.getPassword(), tmp.getFullName(), "0"));
		userManagement.writeFile();
	}

	public static void editProduct(WatchManagement watchManagement, Scanner scanner) {
		displayWatchlist(watchManagement);
		System.out.print("Enter id: ");
		String id;
		int index;
		while (true) {
			id = scanner.nextLine();
			index = watchManagement.getWatchList().findIndex(id);
			if (index != -1) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		Watches tmp = (Watches) watchManagement.getWatchList().get(index);
		System.out.print("Enter new price :");
		String newprice = (CheckInput.toStrNumberic(scanner.nextLine()));
		System.out.print("Enter new quantity :");
		String newquantity = (CheckInput.toStrNumberic(scanner.nextLine()));
		if (newprice != null) {
			tmp.setPrice(Double.parseDouble(newprice));
		}
		if (newquantity != null) {
			tmp.setQuantity(Integer.parseInt(newquantity));
		}
		watchManagement.getWatchList().set(index, tmp);
	}

	public static void deleteProduct(WatchManagement watchManagement, Scanner scanner) {
		displayWatchlist(watchManagement);
		System.out.print("Enter id: ");
		String id;
		int index;
		while (true) {
			id = scanner.nextLine();
			index = watchManagement.getWatchList().findIndex(id);
			if (index != -1) {
				break;
			}
			System.out.print("Id already exists. Enter again: ");
		}
		watchManagement.getWatchList().remove(index);
	}
}
