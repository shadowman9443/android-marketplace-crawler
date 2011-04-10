class CreateAppTargets < ActiveRecord::Migration
  def self.up
    create_table :app_targets do |t|
      t.references :app
      t.references :target

      t.timestamps
    end
    add_index :app_targets, :app_id
    add_index :app_targets, :target_id
  end

  def self.down
    drop_table :app_targets
  end
end
