/*
 _   _      _     _               _             _
| | | |_ __(_)___| |__   ___  ___| | _____  ___| |__
| |_| | '__| / __| '_ \ / _ \/ _ \ |/ / _ \/ __| '_ \
|  _  | |  | \__ \ | | |  __/  __/   <  __/\__ \ | | |
|_| |_|_|  |_|___/_| |_|\___|\___|_|\_\___||___/_| |_|

 ____                  _      _
|  _ \  __ _ _ __   __| | ___| | ____ _ _ __
| | | |/ _` | '_ \ / _` |/ _ \ |/ / _` | '__|
| |_| | (_| | | | | (_| |  __/   < (_| | |
|____/ \__,_|_| |_|\__,_|\___|_|\_\__,_|_|

GrNo: 22010058
Roll: 221018
A   : A1

The hash function I have used on the string is costly for 
small tables but works best when table size is very big 
( Ignoring the drawback of Linear Probing method )
This implementation should be much faster than standard array with
linear search

*/

import java.util.*;
import java.io.*;

public class HashTableLinearProbing 
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("--- Welcome to Hash Table implementation with linear probing ---\n");
		int max_size = -1;
		while( max_size < 1 )
		{
			System.out.println("Enter Max table size: ");
			max_size = sc.nextInt();
			if( max_size < 1 )
			{
				System.out.println("[ ERROR ] Table size needs to be greater than 0");
				System.out.println("Try again...");
			}
		}

		//Hash table implementation object
		Table_Operations hash_run = new Table_Operations(max_size);

		int choice = -1;
		while (choice != 5)
		{
			//Menu Display
			System.out.println(
					"\n----------------- \n" +
					"Insert ---- ( 1 )\n" +
					"Delete ---- ( 2 )\n" +
					"Display --- ( 3 )\n" +
					"Search ---- ( 4 )\n" +
					"Exit ------ ( 5 )\n" +
					"----------------- \n" +
					"\nEnter your choice: "
					);

			//Choice Input and error handeling
			try 
			{
				choice = sc.nextInt();
			}
			catch(Exception ex) //Making sure int is entered
			{
				System.out.println("[ ERROR ] bad input, int expected");
				String x = sc.next(); //Clearing Input Buffer
				continue;
			}
			
			switch (choice)
			{
				case 1:
					{
						hash_run.insert();
					} break;

				case 2:
					{	
						hash_run.delete();
					} break;

				case 3:
					{
						hash_run.display();
					} break;

				case 4:
					{
						hash_run.search();
					} break;

				case 5:
					{
						System.out.println("GoodByee ^_^\n");
					} break;

				default:
					{
						System.out.println("\nWrong choice, Choice range (int) [1-4]\n");
					} break;
			
			}
		}
	}
}

class Table_Operations 
{
	int table_size = 0;
	int live_elements = 0;
	Table hash_table[]; //Size will be assigned later

	Table_Operations(int max_size)
	{
		table_size = max_size;

		//Allocating array
		hash_table = new Table[table_size];
		
		//Allocating elements
		for( int i = 0; i < table_size; i++ ) 
		{
			hash_table[i] = new Table();
		}
	}

	void insert()
	{
		Scanner sc = new Scanner(System.in);
		//Guard Clause
		if( live_elements == table_size )
		{
			System.out.println("[ TABLE FULL ] delete some elements to make space");
			return;
		}

		//Taking input
		Table to_insert = new Table();

		//Clearing buffer
		//String clear_buff = sc.nextLine();

		System.out.println("Enter the name for your record: ");
		to_insert.name = sc.nextLine();	
		to_insert.filled = true;
		
		//Hashing
		int index_key = hash(to_insert);

		//Checking if the position is filled
		if( hash_table[index_key].filled == false )
		{
			hash_table[index_key] = to_insert;	
			System.out.println("[ INSERTED ]");
			live_elements++;
		}
		else
		{
			//Linear Probing
			for( int i = 0; i < table_size; i++ )
			{
				if( hash_table[index_key % table_size].filled == false )
				{
					hash_table[index_key % table_size] = to_insert;
					System.out.println("[ INSERTED ]");
					live_elements++;
					break;
				}
				index_key++;
			}
		}
		
	}

	void delete()
	{
		Scanner sc = new Scanner(System.in);
		
		//Guard Clause
		if( live_elements == 0 )
		{
			System.out.println("[ TABLE EMPTY ]");
			return;
		}

		//Taking input
		Table to_delete = new Table();

		//Clearing buffer
		//String clear_buff = sc.nextLine();

		System.out.println("Enter the name for your record to delete: ");
		to_delete.name = sc.nextLine();	
		//to_delete.filled = true;
		
		//Hashing
		int index_key = hash(to_delete);

		boolean found = false;
		//Checking if the position is filled
		if( hash_table[index_key].filled == true &&
			hash_table[index_key].name.equals(to_delete.name)  )
		{
			hash_table[index_key].filled = false;	
			System.out.println("[ DELETED ]");
			live_elements--;
			found = true;
		}
		else
		{
			//Linear Probing
			for( int i = 0; i < table_size; i++ )
			{
				if( hash_table[index_key % table_size].filled == true &&
					hash_table[index_key % table_size].name.equals(to_delete.name)  )
				{
					hash_table[index_key % table_size].filled = false;
					System.out.println("[ DELETED ]");
					live_elements--;
					found = true;
					break;
				}
				index_key++;
			}

			if( found == false )
			{
				System.out.println("[ ENTERED RECOED WAS NOT IN TABLE ]");
			}
		}
	}

	void display()
	{
		System.out.println("--- Welcome to display ---");

		for( int i = 0; i < table_size; i++ )
		{
			System.out.println("+ --- --- --- --- --- +");
			System.out.println("Table Index: " + i);
			if( hash_table[i].filled == false )
			{
				System.out.println("[ RECORD EMPTY ]");
			}
			else
			{
				System.out.println("Name: " + hash_table[i].name);
			}
			System.out.println("+ --- --- --- --- --- +");
		}
	}

	void search()
	{
		Scanner sc = new Scanner(System.in);
		
		//Guard Clause
		if( live_elements == 0 )
		{
			System.out.println("[ TABLE EMPTY ]");
			return;
		}

		//Taking input
		Table to_search = new Table();

		//Clearing buffer
		//String clear_buff = sc.nextLine();

		System.out.println("Enter the name for your record to search: ");
		to_search.name = sc.nextLine();	
		//to_search.filled = true;
		
		//Hashing
		int index_key = hash(to_search);

		boolean found = false;
		//Checking if the position is filled
		if( hash_table[index_key].filled == true &&
			hash_table[index_key].name.equals(to_search.name)  )
		{
			System.out.println("[ FOUND ]");
			System.out.println("Table Address of record: " + index_key);
			found = true;
		}
		else
		{
			//Linear Probing
			for( int i = 0; i < table_size; i++ )
			{
				if( hash_table[index_key % table_size].filled == true &&
					hash_table[index_key % table_size].name.equals(to_search.name)  )
				{
					System.out.println("[ FOUND ]");
					System.out.println("Table Address of record: " + index_key % table_size);
					found = true;
					break;
				}
				index_key++;
			}

			if( found == false )
			{
				System.out.println("[ ENTERED RECOED WAS NOT IN TABLE ]");
			}
		}
	}

	int hash(Table to_hash)
	{
		int hash = 0;
		//Multiplying with a prime number usually gives more unique Hashes
		for( int i = 0; i < to_hash.name.length(); i++ )
		{
			hash += to_hash.name.charAt(i) * 31; 
		}
		hash %= table_size; //Should not exceed table size
		//System.out.println("hash: " + hash); //For debugging
		return hash;
	}
}
				
class Table 
{
	String name = new String();
	boolean filled;

	Table() 
	{
		filled = false; 
	}
}






